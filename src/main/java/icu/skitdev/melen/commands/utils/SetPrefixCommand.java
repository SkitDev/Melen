package icu.skitdev.melen.commands.utils;

import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)){
            RecurrentAction.noPerm(event);
            return;
        }
        if (args.size() == 0){
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Merci d'indiquer le nouveau prefix");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        Document newDoc = new Document();
        newDoc.append("prefix", args.get(0));
        Melen.database.updateDocument("guilds", "guildId", event.getGuild().getIdLong(), newDoc);
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Succès");
        eb.setDescription("Le nouveau prefix est : " + args.get(0));
        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getUsage() {
        return "<prefix>";
    }

    @Override
    public String getInvoke() {
        return "setprefix";
    }

    @Override
    public String getDescription() {
        return "Permet de changer le prefix du bot sur un serveur discord";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }
}
