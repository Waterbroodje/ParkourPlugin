package me.waterbroodje.parkourplugin.listeners;

import me.waterbroodje.parkourplugin.Main;
import me.waterbroodje.parkourplugin.database.MongoDatabase;
import me.waterbroodje.parkourplugin.domain.Checkpoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

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

        if (!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            if (Main.getInstance().checkpoints.get(Checkpoint.START).equals(e.getTo().getBlock()) && !Main.getInstance().seconds.containsKey(player.getUniqueId())) {

                player.sendMessage(Main.chat(Main.getInstance().getConfig().getString("start-message")));
                Main.getInstance().seconds.put(player.getUniqueId(), 0);
                List<Block> list = new ArrayList<>();
                list.add(e.getTo().getBlock());
                Main.getInstance().lastCheckpoint.put(player.getUniqueId(), list);

            } else if ((Main.getInstance().checkpoints.get(Checkpoint.CHECKPOINT_1).equals(e.getTo().getBlock()) || Main.getInstance().checkpoints.get(Checkpoint.CHECKPOINT_2).equals(e.getTo().getBlock())) && Main.getInstance().seconds.containsKey(player.getUniqueId())) {

                if (Main.getInstance().lastCheckpoint.containsKey(player.getUniqueId())) {
                    if (Main.getInstance().lastCheckpoint.get(player.getUniqueId()).contains(e.getTo().getBlock())) return;
                }

                player.sendMessage(Main.chat(Main.getInstance().getConfig().getString("checkpoint-message")));
                Main.getInstance().lastCheckpoint.get(player.getUniqueId()).add(e.getTo().getBlock());
            } else if (Main.getInstance().checkpoints.get(Checkpoint.END).equals(e.getTo().getBlock()) && Main.getInstance().seconds.containsKey(player.getUniqueId())) {

                if (Main.getInstance().lastCheckpoint.get(player.getUniqueId()).contains(Main.getInstance().checkpoints.get(Checkpoint.CHECKPOINT_1)) && Main.getInstance().lastCheckpoint.get(player.getUniqueId()).contains(Main.getInstance().checkpoints.get(Checkpoint.CHECKPOINT_2))) {

                    int seconds = Main.getInstance().seconds.get(player.getUniqueId());
                    player.sendMessage(Main.chat(Main.getInstance().getConfig().getString("end-message").replace("%time%", seconds + "")));
                    Main.getInstance().seconds.remove(player.getUniqueId());
                    Main.getInstance().lastCheckpoint.remove(player.getUniqueId());
                    MongoDatabase.updateTime(player.getUniqueId(), seconds);

                } else {
                    player.sendMessage(Main.chat(Main.getInstance().getConfig().getString("missing-checkpoints-message")));
                }
            }

        }
    }
}
