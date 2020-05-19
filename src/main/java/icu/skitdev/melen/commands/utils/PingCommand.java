package icu.skitdev.melen.commands.utils;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class PingCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Pong :ping_pong:");
        event.getJDA().getRestPing().queue((ping) -> {
            eb.addField("Rest ping :", String.format("%dms", ping), false);
            eb.addField("Gateway ping :", String.format("%dms", event.getJDA().getGatewayPing()), false);
            event.getChannel().sendMessage(eb.build()).queue();
        });

    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Permet d'obtenir le ping du bot ainsi que le ping de l'API";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }
}
