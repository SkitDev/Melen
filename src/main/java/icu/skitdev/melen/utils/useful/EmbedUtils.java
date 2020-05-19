package icu.skitdev.melen.utils.useful;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.time.LocalDateTime;


public class EmbedUtils {
    public static EmbedBuilder embedGenerator(JDA jda, String title){
        return new EmbedBuilder().setColor(Constants.randomColor()).setFooter(jda.getSelfUser().getName(),jda.getSelfUser().getAvatarUrl()).setTitle("**" + title + "**").setTimestamp(LocalDateTime.now());
    }
}
