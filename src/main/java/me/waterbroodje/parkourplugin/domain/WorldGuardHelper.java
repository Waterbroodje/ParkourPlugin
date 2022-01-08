package me.waterbroodje.parkourplugin.domain;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;

public class WorldGuardHelper {

    private final RegionContainer worldEditContainer;

    public WorldGuardHelper() {
        worldEditContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    public boolean isInRegion(Location location, String region) {
        RegionManager regions = worldEditContainer.get(BukkitAdapter.adapt(location.getWorld()));

        if (regions == null) return false;
        if (regions.getRegion(region) == null) return false;

        return regions.getRegion(region).contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
