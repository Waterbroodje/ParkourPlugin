package me.waterbroodje.parkourplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.waterbroodje.parkourplugin.database.MongoDatabase;
import me.waterbroodje.parkourplugin.domain.WorldGuardHelper;
import me.waterbroodje.parkourplugin.listeners.PlayerMoveListener;
import me.waterbroodje.parkourplugin.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static WorldGuardHelper worldGuardHelper;
    private static ScoreboardManager scoreboardManager;
    private File configFile;

    public final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Map<String, Object> map = new HashMap<>();

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

        MongoDatabase.connect();
        scoreboardManager.runUpdateTask();

        registerEvents(
                new PlayerMoveListener()
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
}
