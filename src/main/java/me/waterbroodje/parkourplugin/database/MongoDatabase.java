package me.waterbroodje.parkourplugin.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import me.waterbroodje.parkourplugin.Main;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

public class MongoDatabase {

    private static MongoCollection<Document> timeCollection;
    private static List<String> list;

    public static void connect() {
        try {
            com.mongodb.client.MongoDatabase database = new MongoClient(new MongoClientURI(Main.getInstance().getConfig().getString("mongodbConnection")))
                    .getDatabase("parkourplugin");

            timeCollection = database.getCollection("times");
            list = new ArrayList<>();
        } catch (Exception exception) {
            System.out.println(ChatColor.RED + "Connection to MongoDB failed. Exception: " + exception);
        }
    }

    public static void updateTime(UUID uuid, int time) {
        try {
            if (getTime(uuid) != 0) {
                if (time > getTime(uuid)) return;
                timeCollection.updateOne(new Document("uuid", uuid.toString()), new Document("$set", new Document("time", time)),
                        new UpdateOptions().upsert(true));
            } else {
                timeCollection.updateOne(new Document("uuid", uuid.toString()), new Document("$set", new Document("time", time)),
                        new UpdateOptions().upsert(true));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String getTimeFormatted(UUID uuid) {
        Document document = timeCollection.find(new Document("uuid", uuid.toString())).first();

        if (document != null) {
            int time = document.getInteger("time");

            if (time < 60) return time + "s";

            BigDecimal secondsValue = BigDecimal.valueOf(time);
            Duration dur = Duration.ofSeconds(secondsValue.longValueExact());
            int minutes = dur.toMinutesPart();
            int seconds = dur.toSecondsPart();

            return String.format("%dm %ds%n", minutes, seconds);
        } else {
            return "0";
        }
    }

    public static int getTime(UUID uuid) {
        Document document = timeCollection.find(new Document("uuid", uuid.toString())).first();

        if (document != null) {
            return document.getInteger("time");
        } else {
            return 0;
        }
    }

    public static List<String> getLeaderboardFormatted() {
        FindIterable<Document> cursor = timeCollection.find().sort(new Document("time", 1)).limit(5);
        resetList();

        int c = 5;
        for (Document document : cursor) {
            list.set(c - 1, Main.chat("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(document.getString("uuid"))).getName() + " &7- &f" + getTimeFormatted(UUID.fromString(document.getString("uuid")))));
            c--;
        }
        return list;
    }

    private static void resetList() {
        list.clear();
        for (int i = 0; i < 5; i++) {
            list.add(i, null);
        }
    }

}