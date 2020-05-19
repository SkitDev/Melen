package icu.skitdev.melen.commands.info;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class ServerInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Guild guild = event.getGuild();
        OffsetDateTime dateTime = guild.getTimeCreated();
        String date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());
        StringBuilder sb = new StringBuilder();
        guild.getRoles().forEach(role -> sb.append(role.getName()).append(", "));


        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Quelques informations sur le serveur " + guild.getName());
        eb.addField("Nom :", guild.getName(), false);
        eb.addField("Créateur :", guild.getOwner().getUser().getAsTag(), false);
        eb.addField("Date de création :", date, false);
        eb.addField("Nombre de membres :", String.valueOf(guild.getMembers().size()), false);
        eb.addField("Nombre de salon :", String.valueOf(guild.getChannels().size()), false);
        eb.addField("Roles :", String.format("```%s```", sb.toString().substring(0, sb.toString().length() -2)), false);
        eb.addField("Région :", guild.getRegion().getName(), false);
        eb.addField("Nombre de boost :", String.valueOf(guild.getBoostCount()), false);
        eb.addField("Niveau de boost :", String.valueOf(guild.getBoostTier().getKey()), false);
        eb.setThumbnail(guild.getIconUrl());
        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "serverinfo";
    }

    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur le serveur";
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATIVE;
    }
}
