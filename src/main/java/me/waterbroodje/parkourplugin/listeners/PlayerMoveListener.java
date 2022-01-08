package me.waterbroodje.parkourplugin.listeners;

import me.waterbroodje.parkourplugin.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (Main.getWorldGuardHelper().isInRegion(e.getTo(), "parkour_region")) {
            //todo: add player to scoreboard
        } else if (Main.getWorldGuardHelper().isInRegion(e.getFrom(), "parkour_region")) {
            //todo: remove player from scoreboard
        }
    }
}
