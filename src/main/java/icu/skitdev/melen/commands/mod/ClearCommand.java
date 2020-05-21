package icu.skitdev.melen.commands.mod;

import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClearCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        Member member = event.getMember();
        Member selfMember = event.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MESSAGE_MANAGE)) {
            RecurrentAction.noPerm(event);
            return;
        }
        if (!selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
            RecurrentAction.noSelfPerm(event);
            return;
        }

        if (args.isEmpty()) {
            RecurrentAction.notANumber(event);
            return;
        }
        int amount;
        String arg = args.get(0);

        try {
            amount = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
            RecurrentAction.notANumber(event);
            return;
        }

        if (amount < 2 || amount > 100) {
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Merci d'indiquer un nombre entre 2 et 100");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }

        channel.getIterableHistory()
                .takeAsync(amount)
                .thenApplyAsync((messages) -> {
                    List<Message> goodMessages = messages.stream()
                            .filter((m) -> !m.getTimeCreated().isBefore(
                                    OffsetDateTime.now().minus(2, ChronoUnit.WEEKS)
                            ))
                            .collect(Collectors.toList());
                    channel.purgeMessages(goodMessages);
                    return goodMessages.size();
                })
                .whenCompleteAsync((count, thr) -> {
                    channel.sendMessage(
                            EmbedUtils.embedGenerator(event.getJDA(), "Succès")
                                    .setDescription("Suppression de " + count + " messages !")
                                    .build()).queue((msg) -> msg.delete().queueAfter(10, TimeUnit.SECONDS));

                    Document doc = Melen.database.getDocument("guilds", "guildId", event.getGuild().getIdLong());
                    long logsId = doc.getLong("logsId");

                    Member moderator = event.getMember();
                    OffsetDateTime dateTime = event.getMessage().getTimeCreated();
                    String date = String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());
                    TextChannel logChannel = event.getGuild().getTextChannelById(logsId);
                    if (logChannel == null) return;

                    EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Nouveau clear !");
                    eb.addField("Modérateur :", moderator.getUser().getAsTag(), false);
                    eb.addField("Salon :", channel.getAsMention(), false);
                    eb.addField("Nombre de message supprimé :", count + " messages", false);
                    eb.addField("Date :", date, false);

                    logChannel.sendMessage(eb.build()).queue();
                })
                .exceptionally((thr) -> {
                    EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
                    eb.addField("Message :", thr.getMessage(), false);
                    if (thr.getCause() != null) {
                        eb.addField("Cause :", thr.getCause().getMessage(), false);
                    }

                    channel.sendMessage(eb.build()).queue();
                    return 0;
                });
    }

    @Override
    public String getUsage() {
        return "<nombre>";
    }

    @Override
    public String getInvoke() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Permet de supprimer le nombre de messages spécifié dans un salon";
    }

    @Override
    public Category getCategory() {
        return Category.MODERATION;
    }
}
