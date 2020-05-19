package icu.skitdev.melen.commands.info;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.managers.CommandManager;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class BotInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();
        long uptimeSec = uptime / 1000;
        long hour = uptimeSec / (60 * 60);
        long minutes = (uptimeSec / 60) - (hour * 60);
        long sec = uptimeSec % 60;


        String up = String.format("%d heures %d minutes et %d secondes.", hour, minutes, sec);

        JDA jda = event.getJDA();
        OffsetDateTime dateTime = jda.getSelfUser().getTimeCreated();
        String date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());

        EmbedBuilder eb = EmbedUtils.embedGenerator(jda, "Quelques informations sur le bot");
        eb.addField("Tag :", jda.getSelfUser().getAsTag(), false);
        eb.addField("Uptime :", up, false);
        eb.addField("Date de création :", date , false);
        eb.addField("Guildes :", String.valueOf(event.getJDA().getGuilds().size()), false);
        eb.addField("Utilisateurs :", String.valueOf(event.getJDA().getUsers().size()), false);
        eb.addField("Roles :", String.valueOf(event.getJDA().getRoles().size()), false);
        eb.addField("Commandes :", String.valueOf(CommandManager.commands.size()), false);
        eb.addField("Lien d'invitation :", jda.getInviteUrl(Permission.ADMINISTRATOR), false);
        eb.addField("Faire un don :", "https://paypal.me/pools/c/8oaI7Zjbhv", false);
        eb.addField("Discord :", "Bientôt", false);
        eb.addField("Site Web :", "Bientôt", false);
        eb.setThumbnail(jda.getSelfUser().getAvatarUrl());
        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "botinfo";
    }

    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le bot";
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATIVE;
    }
}
