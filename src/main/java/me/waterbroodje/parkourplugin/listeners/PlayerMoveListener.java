package me.waterbroodje.parkourplugin.listeners;

import me.waterbroodje.parkourplugin.Main;
import me.waterbroodje.parkourplugin.domain.Checkpoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Locale;
import java.util.Objects;

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

        if (Main.getInstance().checkpoints.containsValue(player.getLocation().getBlock())) {
            // player is on checkpoint
            for (Checkpoint checkpoint : Main.getInstance().checkpoints.keySet()) {

                if (checkpoint.equals(Checkpoint.START)) {
                    player.sendMessage(Main.chat(Main.getInstance().getConfig().getString("start-message")));

                } else if (checkpoint.equals(Checkpoint.CHECKPOINT_1) || checkpoint.equals(Checkpoint.CHECKPOINT_2) && !Main.getInstance().lastCheckpoint.get(player.getUniqueId()).equals(player.getLocation().getBlock())) {

                    player.sendMessage(Main.chat(Main.getInstance().getConfig().getString("checkpoint-message")));
                    Main.getInstance().lastCheckpoint.put(player.getUniqueId(), player.getLocation().getBlock());
                } else if (checkpoint.equals(Checkpoint.END)) {
                    //todo: handle the end
                    player.sendMessage(Main.chat(Main.getInstance().getConfig().getString("end-message")));
                }
            }
        }
    }

    private boolean equals(Location location, Location locationb) {
        return Objects.equals(location.getWorld(), locationb.getWorld()) && Objects.equals(location.getBlockX(), locationb.getBlockX()) && Objects.equals(location.getBlockY(), locationb.getBlockY()) && Objects.equals(location.getBlockZ(), locationb.getBlockZ());
    }
}
