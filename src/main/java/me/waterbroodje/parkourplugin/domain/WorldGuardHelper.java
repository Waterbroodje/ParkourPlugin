package me.waterbroodje.parkourplugin.domain;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.managers.RegionManager;
import me.waterbroodje.parkourplugin.Main;
import org.bukkit.Location;

public class WorldGuardHelper {

    public boolean isInRegion(Location location, String region) {
        RegionManager regions = Main.getWorldEditContainer().get(BukkitAdapter.adapt(location.getWorld()));

        if (regions == null) return false;
        if (regions.getRegion(region) == null) return false;

        return regions.getRegion(region).contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
