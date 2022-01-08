package me.waterbroodje.parkourplugin.managers;

import me.waterbroodje.parkourplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreboardManager {

    public List<UUID> scoreboardPlayers = new ArrayList<>();

    public void addScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("scoreboard", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(Main.chat("&e&lParkour"));

        obj.getScore("&1").setScore(9);

        Team bestAttempt = board.registerNewTeam("bestAttempt");
        bestAttempt.addEntry(ChatColor.BLACK + "");
        bestAttempt.setPrefix(Main.chat(" &e"));
        obj.getScore(ChatColor.BLACK +"").setScore(8);

        Team lb1 = board.registerNewTeam("leaderboard1");
        lb1.addEntry(ChatColor.RED + "");
        lb1.setPrefix(Main.chat(" #6 - "));
        obj.getScore(ChatColor.RED + "").setScore(1);

        player.setScoreboard(board);
        scoreboardPlayers.add(player.getUniqueId());
    }

    private void runUpdateTask(Player player, Scoreboard board) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (scoreboardPlayers.contains(player.getUniqueId())) {
                    board.getTeam("leaderboard").setPrefix("");
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}
