package icu.skitdev.melen.commands.other;


import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.Constants;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class ShutdownCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(Constants.OWNER)){
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Attention");
            eb.setDescription("Le bot se met à jour...");

            event.getChannel().sendMessage(eb.build()).complete();

            event.getJDA().shutdown();
            System.exit(0);
        }else {
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Tu n'as pas la permission de faire cela !");

            event.getChannel().sendMessage(eb.build()).queue();
        }
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getInvoke() {
        return "shutdown";
    }

    @Override
    public String getDescription() {
        return "Permet de mettre a jour le bot";
    }

    @Override
    public Category getCategory() {
        return Category.AUTRE;
    }
}
