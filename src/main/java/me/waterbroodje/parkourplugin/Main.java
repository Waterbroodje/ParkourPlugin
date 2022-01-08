package me.waterbroodje.parkourplugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.waterbroodje.parkourplugin.listeners.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static RegionContainer worldEditContainer;


    @Override
    public void onEnable() {
        instance = this;
        worldEditContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();

        registerEvents(
                new PlayerMoveListener()
        );
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return instance;
    }

    public static RegionContainer getWorldEditContainer() {
        return worldEditContainer;
    }

    public void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener ->
                Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
