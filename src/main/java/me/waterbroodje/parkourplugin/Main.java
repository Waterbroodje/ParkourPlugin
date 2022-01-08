package me.waterbroodje.parkourplugin;

import me.waterbroodje.parkourplugin.database.MongoDatabase;
import me.waterbroodje.parkourplugin.domain.WorldGuardHelper;
import me.waterbroodje.parkourplugin.listeners.PlayerMoveListener;
import me.waterbroodje.parkourplugin.managers.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static WorldGuardHelper worldGuardHelper;
    private static ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        instance = this;
        worldGuardHelper = new WorldGuardHelper();
        scoreboardManager = new ScoreboardManager();

        MongoDatabase.connect();

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
