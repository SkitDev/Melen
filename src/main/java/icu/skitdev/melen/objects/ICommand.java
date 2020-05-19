package icu.skitdev.melen.objects;


import icu.skitdev.melen.enumerations.Category;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public interface ICommand {

    void handle(List<String> args, GuildMessageReceivedEvent event);

    String getUsage();

    String getInvoke();

    String getDescription();

    Category getCategory();
}
