package icu.skitdev.melen.commands.music;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class JoinCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        if (Utils.joinAVoiceChannel(event, member, true))
            event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Connexion au salon vocal en cours...").build()).queue();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Permet au bot de rejoindre ton salon vocal";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
