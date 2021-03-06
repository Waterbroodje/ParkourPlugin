package me.waterbroodje.parkourplugin.managers;

import me.waterbroodje.parkourplugin.Main;
import me.waterbroodje.parkourplugin.database.MongoDatabase;
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
        player.setScoreboard(this.loadScoreboard(player.getUniqueId()));
        scoreboardPlayers.add(player.getUniqueId());
    }

    public void runUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> list = MongoDatabase.getLeaderboardFormatted();
                for (UUID uuid : scoreboardPlayers) {
                    if (Bukkit.getPlayer(uuid) == null) { scoreboardPlayers.remove(uuid); return;}

                    Scoreboard board = Bukkit.getPlayer(uuid).getScoreboard();
                    board.getTeam("bestAttempt").setPrefix(Main.chat("&e&lBest Attempt&f: " + MongoDatabase.getTimeFormatted(uuid)));
                    board.getTeam("leaderboard5").setPrefix(Main.chat(list.get(0) == null ? "&e #5 &7- &enone &7- &f0s" : "&e #5 " + list.get(0)));
                    board.getTeam("leaderboard4").setPrefix(Main.chat(list.get(1) == null ? "&e #4 &7- &enone &7- &f0s" : "&e #4 " + list.get(1)));
                    board.getTeam("leaderboard3").setPrefix(Main.chat(list.get(2) == null ? "&e #3 &7- &enone &7- &f0s" : "&e #3 " + list.get(2)));
                    board.getTeam("leaderboard2").setPrefix(Main.chat(list.get(3) == null ? "&e #2 &7- &enone &7- &f0s" : "&e #2 " + list.get(3)));
                    board.getTeam("leaderboard1").setPrefix(Main.chat(list.get(4) == null ? "&e #1 &7- &enone &7- &f0s" : "&e #1 " + list.get(4)));
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20 * 5);
    }

    public Scoreboard loadScoreboard(UUID uuid) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("scoreboard", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(Main.chat("&e&lParkour"));

        obj.getScore(Main.chat("&1")).setScore(9);

        Team bestAttempt = board.registerNewTeam("bestAttempt");
        bestAttempt.addEntry(ChatColor.BLACK + "");
        bestAttempt.setPrefix(Main.chat("&e&lBest Attempt&f: &f" + MongoDatabase.getTimeFormatted(uuid)));
        obj.getScore(Main.chat(ChatColor.BLACK + "")).setScore(8);

        obj.getScore(Main.chat("&2")).setScore(7);

        obj.getScore(Main.chat("&e&lLeaderboard&f:")).setScore(6);

        List<String> list = MongoDatabase.getLeaderboardFormatted();

        Team lb1 = board.registerNewTeam("leaderboard1");
        lb1.addEntry(ChatColor.GREEN + "");
        lb1.setPrefix(Main.chat(list.get(4) == null ? "&e #1 &7- &enone &7- &f0s" : "&e #1 " + list.get(4)));
        obj.getScore(ChatColor.GREEN + "").setScore(5);

        Team lb2 = board.registerNewTeam("leaderboard2");
        lb2.addEntry(ChatColor.RED + "");
        lb2.setPrefix(Main.chat(list.get(3) == null ? "&e #2 &7- &enone &7- &f0s" : "&e #2 " + list.get(3)));
        obj.getScore(ChatColor.RED + "").setScore(4);

        Team lb3 = board.registerNewTeam("leaderboard3");
        lb3.addEntry(ChatColor.BLUE + "");
        lb3.setPrefix(Main.chat(list.get(2) == null ? "&e #3 &7- &enone &7- &f0s" : "&e #3 " + list.get(2)));
        obj.getScore(ChatColor.BLUE + "").setScore(3);

        Team lb4 = board.registerNewTeam("leaderboard4");
        lb4.addEntry(ChatColor.GRAY + "");
        lb4.setPrefix(Main.chat(list.get(1) == null ? "&e #4 &7- &enone &7- &f0s" : "&e #4 " + list.get(1)));
        obj.getScore(ChatColor.GRAY + "").setScore(2);

        Team lb5 = board.registerNewTeam("leaderboard5");
        lb5.addEntry(ChatColor.GOLD + "");
        lb5.setPrefix(Main.chat(list.get(0) == null ? "&e #5 &7- &enone &7- &f0s" : "&e #5 " + list.get(0)));
        obj.getScore(ChatColor.GOLD + "").setScore(1);

        return board;
    }
}
