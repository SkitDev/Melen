package icu.skitdev.melen.utils.useful;


import icu.skitdev.melen.utils.JDAManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.json.JSONArray;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Games {


    public void timedGames(){
        JDA jda = JDAManager.getJDA();
        JSONArray games = Constants.GAMES;
        Timer timer = new Timer();
        Random rdm = new Random();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching(
                        games.getString(
                                rdm.nextInt(games.length())
                        ).replaceAll("%guilds%", String.valueOf(jda.getGuilds().size()))
                        .replaceAll("%users%", String.valueOf(jda.getUsers().size()))
                        .replaceAll("%roles%", String.valueOf(jda.getRoles().size()))
                ));
            }
        };
        timer.schedule(task, 1000, 10000);
    }


}
