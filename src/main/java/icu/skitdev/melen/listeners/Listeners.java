package icu.skitdev.melen.listeners;


import icu.skitdev.melen.Melen;
import icu.skitdev.melen.managers.CommandManager;
import icu.skitdev.melen.utils.Databases;
import icu.skitdev.melen.utils.useful.Constants;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;


public class Listeners extends ListenerAdapter {

    private final CommandManager manager;

    public Listeners(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().isWebhookMessage() || event.getAuthor().isFake()) return;
        Document doc = Melen.database.getDocument("guilds",  "guildId",event.getGuild().getIdLong());
        String prefix = doc.getString("prefix") != null ? doc.getString("prefix") : Constants.PREFIX;
        if (event.getMessage().getContentRaw().startsWith(prefix)) {
            event.getMessage().delete().queue();
            event.getChannel().sendTyping().queue();
            manager.handleCommand(event);

        }
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        Guild guild = event.getGuild();
        Databases database = Melen.database;
        if (!database.isExists("guilds", "guildId", guild.getIdLong())) {
            Document doc = new Document();
            doc.append("guildId", guild.getIdLong());
            database.insertDocument("guilds", doc);
        }
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Bonjour !");
        eb.setDescription(String.format("Moi c'est %s !\nJe suis un bot Multifonction crée par %s !\nJe suis en cours de développement mais je suis fier que vous m'ayez invité sur votre serveur !", event.getJDA().getSelfUser().getName(), event.getJDA().getUserById(Constants.OWNER).getAsTag()));
        Objects.requireNonNull(event.getGuild().getDefaultChannel()).sendMessage(eb.build()).queue();

    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Document doc = Melen.database.getDocument("guilds", "guildId", event.getGuild().getIdLong());
        String welcomeMessage = doc.getString("welcomeMessage");
        if (welcomeMessage == null) return;
        welcomeMessage = welcomeMessage.replaceAll("%users%", String.valueOf(event.getGuild().getMembers().size()))
                .replaceAll("%member%", event.getMember().getAsMention())
                .replaceAll("%tag%", event.getUser().getAsTag())
                .replaceAll("%guild%", event.getGuild().getName())
                .replaceAll("\\|", System.lineSeparator());
        TextChannel channel = event.getJDA().getTextChannelById(doc.getLong("welcomeId"));
        if (channel == null) return;
        channel.sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Nouveau membre").setDescription(welcomeMessage).build()).queue();
    }

}
