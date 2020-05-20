package icu.skitdev.melen.utils.useful;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static boolean joinAVoiceChannel(GuildMessageReceivedEvent event, Member member, boolean isJoin) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            if (isJoin) {
                RecurrentAction.alreadyConnected(event);
                return false;
            }
            return true;
        }
        GuildVoiceState voiceState = member.getVoiceState();

        if (!voiceState.inVoiceChannel()) {
            RecurrentAction.notInAVoiceChannel(event);
            return false;
        }
        VoiceChannel voiceChannel = voiceState.getChannel();
        Member selfMember = event.getGuild().getSelfMember();
        if (!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            RecurrentAction.noSelfPerm(event);
            return false;
        }
        audioManager.openAudioConnection(voiceChannel);
        return true;
    }

    public static boolean leave(GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        if (!audioManager.isConnected()) {
            RecurrentAction.notConnected(event);
            return false;
        }
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();
        if (!voiceChannel.getMembers().contains(event.getMember())) {
            RecurrentAction.notInTheSameChannel(event);
            return false;
        }
        audioManager.closeAudioConnection();
        return true;
    }

    public static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);


        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
