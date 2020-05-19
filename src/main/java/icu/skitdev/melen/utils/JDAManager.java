package icu.skitdev.melen.utils;

import icu.skitdev.melen.utils.useful.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class JDAManager {
    private static final JDA jda = buildJDA();


    public static JDA getJDA() {
        return jda;
    }

    private static JDA buildJDA() {
        Logger logger = LoggerFactory.getLogger(JDAManager.class);
        try {
            return new JDABuilder()
                    .setToken(Constants.TOKEN)
                    .build()
                    .awaitReady();
        } catch (LoginException | InterruptedException e) {
            logger.error("Token invalide ou connexion a Discord impossible !");
            return null;
        }
    }
}
