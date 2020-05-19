package icu.skitdev.melen.commands.mod;

import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MuteCommand implements ICommand {
    private static final Logger logger = LoggerFactory.getLogger(MuteCommand.class);
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String reason = "";
        OffsetDateTime dateTime = event.getMessage().getTimeCreated();
        String date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());
        Member moderator = event.getMember();
        if(!moderator.hasPermission(Permission.MANAGE_ROLES)){
            RecurrentAction.noPerm(event);
            return;
        }
        if (args.isEmpty() || event.getMessage().getMentionedUsers().size() == 0) {
            RecurrentAction.noUserMention(event);
            return;
        }
        Member muted = event.getMessage().getMentionedMembers().get(0);


        if (!moderator.canInteract(muted)) {
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Tu ne peux pas réduire au silence cette personne !");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if (!event.getGuild().getSelfMember().canInteract(muted)) {
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Je ne peux pas réduire au silence cette personne !");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if (args.size() > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.size(); i++) {
                sb.append(args.get(i)).append(" ");
            }
            reason = sb.toString();
        }
        if(event.getGuild().getRolesByName("muted", true).size() == 0){
            event.getGuild().createRole().setName("Muted").setColor(Color.lightGray).setPermissions(Permission.EMPTY_PERMISSIONS).setMentionable(false).setHoisted(false).complete();
            event.getGuild().getChannels().forEach(chan -> {
                chan.getManager().putPermissionOverride(event.getGuild().getRolesByName("muted", true).get(0), null, Collections.singleton(Permission.MESSAGE_WRITE)).queue();
            });
        }
        Role muteRole = event.getGuild().getRolesByName("muted", true).get(0);
        if(muted.getRoles().contains(muteRole)){
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Cette personne est déjà réduite au silence !");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        final boolean[] erro = {false};
        event.getGuild().addRoleToMember(muted, muteRole).submit().whenComplete((role, err) -> {
           if(err != null) {
               logger.error(err.getMessage());
               event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Une erreur est survenue lors de la réduction au silence de cette personne !").build()).queue();
               erro[0] = true;
               return;
           }
               event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Tu as bien réduis au silence " + muted.getUser().getAsTag() + " !").build()).queue();
        });
        if (!erro[0]){
            Document doc = Melen.database.getDocument("guilds",  "guildId", event.getGuild().getIdLong());
            long logChannel = doc.getLong("logsId");

            TextChannel channel = event.getGuild().getTextChannelById(logChannel);
            if (channel == null) return;

            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Personne réduit au silence !");
            eb.addField("Personne réduit au silence :", muted.getUser().getAsTag(), false);
            eb.addField("Modérateur :", moderator.getUser().getAsTag(), false);
            if (!reason.isEmpty()) eb.addField("Raison :", reason, false);
            eb.addField("Date :", date, false);

            channel.sendMessage(eb.build()).queue();
        }
    }

    @Override
    public String getUsage() {
        return "<@utilisateur> [raison]";
    }

    @Override
    public String getInvoke() {
        return "mute";
    }

    @Override
    public String getDescription() {
        return "Permet de réduire au silence quelqu'un";
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
