package icu.skitdev.melen;

import icu.skitdev.melen.listeners.Listeners;
import icu.skitdev.melen.managers.CommandManager;
import icu.skitdev.melen.utils.Databases;
import icu.skitdev.melen.utils.JDAManager;
import icu.skitdev.melen.utils.config.Configuration;
import icu.skitdev.melen.utils.useful.Constants;
import icu.skitdev.melen.utils.useful.Games;
import net.dv8tion.jda.api.JDA;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Melen {
    private static final Logger logger = LoggerFactory.getLogger(Melen.class);
    private static final CommandManager commandManager = new CommandManager();
    private static final Listeners listener = new Listeners(commandManager);
    public static final Configuration CONFIGURATION;
    public static Databases database;

    static {
        Configuration configuration = null;

        try {
            configuration = new Configuration("config.json");
        } catch (IOException e) {
            logger.error("Impossible de créer le fichier de configuration !");
            e.printStackTrace();
        }
        CONFIGURATION = configuration;
    }

    public static void main(String[] args) {
        if (CONFIGURATION == null) {
            logger.error("Impossible de charger le fichier de configuration.");
            return;
        }
        generate();
        logger.info("Lancement en cours...");
        if (JDAManager.getJDA() == null) {
            logger.error("Impossible de lancer le bot !");
            CONFIGURATION.save();
            return;
        }
        JDAManager.getJDA().addEventListener(listener);
        Constants.generateGames();
        Games games = new Games();
        games.timedGames();
        database = new Databases();
        initGuilds();
        logger.info("Je suis activé !");
        CONFIGURATION.save();
    }

    private static void generate() {
        logger.info(Constants.PREFIX);
        logger.info(Constants.OWNER);
    }

    private static void initGuilds() {
        JDA jda = JDAManager.getJDA();
        jda.getGuilds().forEach((guild -> {
            if (!database.isExists("guilds",  "guildId",guild.getIdLong())) {
                Document doc = new Document();
                doc.append("guildId", guild.getIdLong());
                database.insertDocument("guilds", doc);
            }
        }));
    }

}
