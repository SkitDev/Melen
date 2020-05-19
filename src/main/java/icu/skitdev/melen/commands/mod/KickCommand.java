package icu.skitdev.melen.commands.mod;

import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class KickCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String reason = "";
        OffsetDateTime dateTime = event.getMessage().getTimeCreated();
        String date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());

        if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            RecurrentAction.noPerm(event);
            return;
        }

        if (args.isEmpty() || event.getMessage().getMentionedUsers().size() == 0) {
            RecurrentAction.noUserMention(event);
            return;
        }

        Member moderator = event.getMember();
        Member kicked = event.getMessage().getMentionedMembers().get(0);
        String name = kicked.getUser().getAsTag();

        if (!moderator.canInteract(kicked)) {
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Tu ne peux pas expulser cette personne !");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().canInteract(kicked)) {
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Je ne peux pas expulser cette personne !");
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

        kicked.kick(reason.isEmpty() ? reason : "No reason").queue();
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Tu as bien expulsé " + name).build()).queue();
        Document doc = Melen.database.getDocument("guilds", "guildId", event.getGuild().getIdLong());
        long logChannel = doc.getLong("logsId");

        TextChannel channel = event.getGuild().getTextChannelById(logChannel);
        if (channel == null) return;

        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Personne expulsé !");
        eb.addField("Personne expulsé :", kicked.getUser().getAsTag(), false);
        eb.addField("Modérateur :", moderator.getUser().getAsTag(), false);
        if (!reason.isEmpty()) eb.addField("Raison :", reason, false);
        eb.addField("Date :", date, false);

        channel.sendMessage(eb.build()).queue();

    }

    @Override
    public String getUsage() {
        return "<@utilisateur> [raison]";
    }

    @Override
    public String getInvoke() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Permet d'expulser quelqu'un du serveur discord";
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
