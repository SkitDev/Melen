package icu.skitdev.melen.utils.useful;


import icu.skitdev.melen.Melen;
import org.json.JSONArray;

import java.awt.*;
import java.util.Random;

public class Constants {
    private static final JSONArray games = new JSONArray();

    public static String TOKEN = Melen.CONFIGURATION.getString("token", "Le token de ton bot");


    public static String PREFIX = Melen.CONFIGURATION.getString("prefix", "le prefix pour ton bot");
    public static String OWNER = Melen.CONFIGURATION.getString("ownerID", "Ton clientID");
    static JSONArray GAMES = Melen.CONFIGURATION.getArray("gameName", games);
    public static String MONGODBURI = Melen.CONFIGURATION.getString("mongodburi", "mongodb://localhost:27017");
    public static String MONGODBDATABASE = Melen.CONFIGURATION.getString("mongodbdatabase", "Melen");
    public static String GITHUBTOKEN = Melen.CONFIGURATION.getString("github", "ton token github");

    public static Color randomColor() {
        Random random = new Random();
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();

        return new Color(r, g, b);
    }

    public static void generateGames() {
        games.put("%guilds% guildes");
        games.put("%users% utilisateurs");
        games.put("%roles% roles");

    }
}
