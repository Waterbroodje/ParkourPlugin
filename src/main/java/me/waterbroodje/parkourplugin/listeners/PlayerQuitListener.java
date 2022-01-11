package me.waterbroodje.parkourplugin.listeners;

import me.waterbroodje.parkourplugin.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (!Main.getInstance().seconds.containsKey(e.getPlayer().getUniqueId())) return;

        Main.getInstance().seconds.remove(e.getPlayer().getUniqueId());
        Main.getInstance().lastCheckpoint.remove(e.getPlayer().getUniqueId());
    }
}
