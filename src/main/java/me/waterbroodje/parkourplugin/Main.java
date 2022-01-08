package me.waterbroodje.parkourplugin;

import me.waterbroodje.parkourplugin.domain.WorldGuardHelper;
import me.waterbroodje.parkourplugin.listeners.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static WorldGuardHelper worldGuardHelper;

    @Override
    public void onEnable() {
        instance = this;
        worldGuardHelper = new WorldGuardHelper();

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

    public void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener ->
                Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
