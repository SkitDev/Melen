package icu.skitdev.melen.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.music.GuildMusicManager;
import icu.skitdev.melen.utils.music.PlayerManager;
import icu.skitdev.melen.utils.music.TrackScheduler;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class SkipCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager guildMusicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = guildMusicManager.scheduler;
        AudioPlayer player = guildMusicManager.player;

        if (player.getPlayingTrack() == null) {
            channel.sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Aucune musique n'est en cours de lecture !").build()).queue();
            return;
        }

        scheduler.nextTrack();
        channel.sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Passage à la musique suivante...").build()).queue();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Permet de passer à la musique suivante";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
