package me.waterbroodje.parkourplugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.waterbroodje.parkourplugin.listeners.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;
    public List<UUID> playerInRegion = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        registerEvents(
                new PlayerMoveListener()
        );
    }

    public static Main getInstance() {
        return instance;
    }

    public void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener ->
                Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
