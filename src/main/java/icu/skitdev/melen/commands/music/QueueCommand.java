package icu.skitdev.melen.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.music.GuildMusicManager;
import icu.skitdev.melen.utils.music.PlayerManager;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (queue.isEmpty()) {
            channel.sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("La file d'attente est vide !").build()).queue();
            return;
        }

        int trackCount = Math.min(queue.size(), 16);

        List<AudioTrack> tracks = new ArrayList<>(queue);
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "File d'attente (Total: " + queue.size() + ")");

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            eb.addField(track.getInfo().title, "Par " + track.getInfo().author, false);
        }

        channel.sendMessage(eb.build()).queue();


    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Permet de voir la liste des musiques dans la file d'attente";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
