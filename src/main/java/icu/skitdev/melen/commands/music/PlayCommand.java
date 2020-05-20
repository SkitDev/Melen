package icu.skitdev.melen.commands.music;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.music.PlayerManager;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import icu.skitdev.melen.utils.useful.Utils;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PlayCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (args.isEmpty()) {
            RecurrentAction.notALink(event);
            return;
        }

        String input = String.join(" ", args);

        if (!isUrl(input) && !input.startsWith("ytsearch:")) {
            RecurrentAction.notALink(event);
            return;
        }
        if (Utils.joinAVoiceChannel(event, event.getMember(), false)) {
            PlayerManager playerManager = PlayerManager.getInstance();
            playerManager.loadAndPlay(channel, input);
            playerManager.getGuildMusicManager(event.getGuild()).player.setVolume(25);
        }

    }

    private boolean isUrl(String input) {
        try {
            new URL(input);
            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Override
    public String getUsage() {
        return "<lien>";
    }

    @Override
    public String getInvoke() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Permet de lancer une musique avec le bot";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
