package icu.skitdev.melen.commands.music;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.Utils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (Utils.leave(event))
            event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Déconnexion du salon vocal en cours...").build()).queue();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Permet de déconnecter le bot d'un salon vocal";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }
}
