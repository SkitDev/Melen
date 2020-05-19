package icu.skitdev.melen.commands.utils;

import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class SuggestionCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(args.size() == 0){
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Merci d'indiquer ta suggestion !");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        TextChannel channel = event.getGuild().getTextChannelById(Melen.database.getDocument("guilds", "guildId", event.getGuild().getIdLong()).getLong("suggestionsId"));
        if(channel == null){
            RecurrentAction.noChannel(event, "suggestion");
            return;
        }
        StringBuilder sb = new StringBuilder();
        args.forEach((arg) -> sb.append(arg).append(" "));
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Nouvelle suggestion");
        eb.setDescription(sb.toString());
        channel.sendMessage(eb.build()).queue();
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Ta suggestion a bien été envoyé !").build()).queue();

    }

    @Override
    public String getUsage() {
        return "<Suggestion>";
    }

    @Override
    public String getInvoke() {
        return "suggestion";
    }

    @Override
    public String getDescription() {
        return "Permet d'envoyer une suggestion";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }
}
