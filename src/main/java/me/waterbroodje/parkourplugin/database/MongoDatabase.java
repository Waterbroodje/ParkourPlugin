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

import java.util.*;

public class MongoDatabase {

    private static MongoCollection<Document> timeCollection;
    private static List<String> list;

    static {
        try {
            com.mongodb.client.MongoDatabase database = new MongoClient(new MongoClientURI(Main.getInstance().getConfig().getString("mongodbConnection")))
                    .getDatabase("parkourplugin");
            timeCollection = database.getCollection("times");
            list = new ArrayList<>();
        } catch (Exception exception) {
            System.out.println(ChatColor.RED + "Connection to MongoDB failed. Exception: " + exception);
        }
    }

    public static void updateTime(UUID uuid, double time) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
                timeCollection.updateOne(new Document("uuid", uuid.toString()), new Document("$set", new Document("time", time)),
                        new UpdateOptions().upsert(true));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static int getTime(UUID uuid) {
        Document document = timeCollection.find(new Document("uuid", uuid.toString())).first();

        if (document.getInteger("time") == null) return 0;
        return document.getInteger("time");
    }

    public static List<String> getLeaderboard() {
        FindIterable<Document> cursor = timeCollection.find().sort(new Document("time", 1)).limit(5);
        resetList();

        int c = 0;
        for (Document document : cursor) {
            list.set(c, Main.chat("l#" + c + 1 + " - " + Bukkit.getOfflinePlayer(document.getString("uuid")).getName() + " - " + document.getInteger("time") + "s"));
            c++;
        }
        return list;
    }

    private static void resetList() {
        list.set(0, null);
        list.set(1, null);
        list.set(2, null);
        list.set(3, null);
        list.set(4, null);
    }
}
