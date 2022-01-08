package me.waterbroodje.parkourplugin.listeners;

import me.waterbroodje.parkourplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (Main.getWorldGuardHelper().isInRegion(e.getTo(), "parkour_region") && !Main.getScoreboardManager().scoreboardPlayers.contains(player.getUniqueId())) {
            Main.getScoreboardManager().addScoreboard(player);
        } else if (Main.getWorldGuardHelper().isInRegion(e.getFrom(), "parkour_region") && !Main.getWorldGuardHelper().isInRegion(e.getTo(), "parkour_region")) {
            Main.getScoreboardManager().scoreboardPlayers.remove(player.getUniqueId());
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }
}
