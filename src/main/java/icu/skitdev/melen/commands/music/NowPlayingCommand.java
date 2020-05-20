package icu.skitdev.melen.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.music.GuildMusicManager;
import icu.skitdev.melen.utils.music.PlayerManager;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class NowPlayingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        AudioPlayer player = guildMusicManager.player;

        if (player.getPlayingTrack() == null) {
            channel.sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Aucune musique n'est en cours de lecture !").build()).queue();
            return;
        }

        AudioTrackInfo info = player.getPlayingTrack().getInfo();

        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Musique en cours de lecture");
        eb.addField("Titre :", info.title, false);
        eb.addField("Auteur :", info.author, false);
        eb.addField("URL :", info.uri, false);
        eb.addField("Durée :", Utils.formatTime(player.getPlayingTrack().getPosition()) + " - " + Utils.formatTime(player.getPlayingTrack().getDuration()), false);


        channel.sendMessage(eb.build()).queue();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "nowplaying";
    }

    @Override
    public String getDescription() {
        return "Permet de voir la musique en cours de lecture";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
