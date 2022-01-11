package me.waterbroodje.parkourplugin;

import com.google.gson.*;
import me.waterbroodje.parkourplugin.database.MongoDatabase;
import me.waterbroodje.parkourplugin.domain.Checkpoint;
import me.waterbroodje.parkourplugin.domain.WorldGuardHelper;
import me.waterbroodje.parkourplugin.listeners.PlayerMoveListener;
import me.waterbroodje.parkourplugin.listeners.PlayerQuitListener;
import me.waterbroodje.parkourplugin.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static WorldGuardHelper worldGuardHelper;
    private static ScoreboardManager scoreboardManager;
    private File configFile;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Map<String, Object> map = new HashMap<>();
    public Map<Checkpoint, Block> checkpoints = new HashMap<>();
    public Map<UUID, List<Block>> lastCheckpoint = new HashMap<>();
    public Map<UUID, Integer> seconds = new HashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        instance = this;
        worldGuardHelper = new WorldGuardHelper();
        scoreboardManager = new ScoreboardManager();

        configFile = new File(getDataFolder(), "checkpoints.json");
        if (!configFile.exists()) saveResource(configFile.getName(), false);
        try {
            map = gson.fromJson(new FileReader(configFile), new HashMap<String, Object>().getClass());

            final String json = gson.toJson(map);
            configFile.delete();
            Files.write(configFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadCheckpoints();
        loadTimer();

        MongoDatabase.connect();
        scoreboardManager.runUpdateTask();

        registerEvents(
                new PlayerMoveListener(),
                new PlayerQuitListener()
        );

    }

    public static Main getInstance() {
        return instance;
    }

    public static WorldGuardHelper getWorldGuardHelper() {
        return worldGuardHelper;
    }

    public static ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener ->
                Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public void loadCheckpoints() {
        String inline = "";
        try {
            Scanner scanner = new Scanner(configFile);
            while (scanner.hasNextLine()) {
                inline += scanner.nextLine() + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonObject object = (JsonObject) new JsonParser().parse(inline);
        JsonArray array = (JsonArray) object.get("checkpointsData");
        for (Object obj : array) {
            if (obj instanceof JsonObject) {
                checkpoints.put(Checkpoint.valueOf(((JsonObject) obj).get("type").getAsString().toUpperCase()),
                        new Location(Bukkit.getWorld(((JsonObject) obj).get("worldName").getAsString()), ((JsonObject) obj).get("x").getAsInt(), ((JsonObject) obj).get("y").getAsInt(), ((JsonObject) obj).get("z").getAsInt()).getBlock());
            }
        }
        System.out.println(checkpoints);
    }

    public void loadTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                seconds.replaceAll((u, v) -> seconds.get(u) + 1);
            }
        }.runTaskTimer(this, 0L, 10L);
    }
}