package icu.skitdev.melen.commands.info;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.isEmpty() || event.getMessage().getMentionedMembers().isEmpty()) {
            RecurrentAction.noUserMention(event);
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);
        OffsetDateTime dateTime = member.getTimeCreated();
        String date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());

        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Quelques informations sur " + member.getUser().getName());
        eb.addField("Tag :", member.getUser().getAsTag(), false);
        eb.addField("A rejoint discord :", date, false);

        StringBuilder sb = new StringBuilder();
        member.getRoles().forEach(role -> sb.append(role.getName()).append(", "));

        StringBuilder sb2 = new StringBuilder();
        member.getPermissions().forEach(permission -> sb2.append(permission.getName()).append(", "));

        StringBuilder sb3 = new StringBuilder();
        member.getActivities().forEach(activity -> sb3.append(activity.getName()).append("\n"));

        dateTime = member.getTimeJoined();
        date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());

        eb.addField("A rejoint le serveur :", date, false);
        if (!sb.toString().equalsIgnoreCase(""))
            eb.addField("Roles : ", String.format("```%s```", sb.toString().substring(0, sb.toString().length() - 2)), false);
        if (!sb2.toString().equalsIgnoreCase(""))
            eb.addField("Permissions : ", String.format("```%s```", sb2.toString().substring(0, sb2.toString().length() - 2)), false);
        eb.addField("Statut : ", member.getOnlineStatus().getKey(), false);
        if (!sb3.toString().equalsIgnoreCase("")) eb.addField("Activité :", sb3.toString(), false);


        eb.setThumbnail(member.getUser().getAvatarUrl());
        event.getChannel().sendMessage(eb.build()).queue();

    }

    @Override
    public String getUsage() {
        return "<Mention>";
    }

    @Override
    public String getInvoke() {
        return "userinfo";
    }

    @Override
    public String getDescription() {
        return "Permet d'obtenir des informations sur un utilisateur";
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATIVE;
    }
}
