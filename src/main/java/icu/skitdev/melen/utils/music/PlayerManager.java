package icu.skitdev.melen.utils.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager instance;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static synchronized PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);
        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                channel.sendMessage(
                        EmbedUtils.embedGenerator(channel.getJDA(), "Ajout d'une musique à la file d'attente...")
                                .addField("Titre :", audioTrack.getInfo().title, false)
                                .addField("Auteur :", audioTrack.getInfo().author, false)
                                .addField("URL :", audioTrack.getInfo().uri, false)
                                .build()

                ).queue();
                play(musicManager, audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack firstTrack = audioPlaylist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = audioPlaylist.getTracks().remove(0);
                }
                channel.sendMessage(
                        EmbedUtils.embedGenerator(channel.getJDA(), "Ajout d'une playlist à la file d'attente...")
                                .addField("Titre :", audioPlaylist.getName(), false)
                                .addField("URL :", trackUrl, false)
                                .addField("Nombre de musique :", String.valueOf(audioPlaylist.getTracks().size()), false)
                                .build()
                ).queue();


                play(musicManager, firstTrack);

                audioPlaylist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                channel.sendMessage(
                        EmbedUtils.embedGenerator(channel.getJDA(), "Erreur")
                                .setDescription("Aucune musique trouvé pour cette recharche :" + trackUrl)
                                .build()
                ).queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage(
                        EmbedUtils.embedGenerator(channel.getJDA(), "Erreur")
                                .setDescription("Impossible de lire cette musique :" + e.getMessage())
                                .build()
                ).queue();
            }
        });
    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }
}
