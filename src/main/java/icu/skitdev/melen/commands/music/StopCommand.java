package icu.skitdev.melen.commands.music;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.music.GuildMusicManager;
import icu.skitdev.melen.utils.music.PlayerManager;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.Utils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class StopCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());

        musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.player.setPaused(false);
        Utils.leave(event);
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Musique stoppé et nettoyage de la file d'attente !").build()).queue();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Permet d'arreter la musique dans un salon vocal";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
