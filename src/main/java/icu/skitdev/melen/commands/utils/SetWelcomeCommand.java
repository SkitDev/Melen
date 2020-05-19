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

public class SetWelcomeCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)){
            RecurrentAction.noPerm(event);
            return;
        }
        if (args.size() == 0){
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
            eb.setDescription("Merci d'indiquer le message de bienvenue!");
            eb.addField("Variables utile :", "**%users%** : Nombres de membres au total sur le serveur\n**%member%** : Mentionne le nouvel arrivant\n**%tag%** : Envoie le tag du nouvel arrivant\n**%guild%** : Envoie le nom de la guilde\n**|** : Fait un retour à la ligne", false);
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        Document newDoc = new Document();
        StringBuilder sb = new StringBuilder();
        args.forEach(arg -> sb.append(arg).append(" "));
        newDoc.append("welcomeMessage", sb.toString());
        Melen.database.updateDocument("guilds", "guildId", event.getGuild().getIdLong(), newDoc);
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Succès");
        eb.setDescription("Le nouveau message de bienvenue est : \n" + sb.toString());
        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getUsage() {
        return "<message>";
    }

    @Override
    public String getInvoke() {
        return "setwelcome";
    }

    @Override
    public String getDescription() {
        return "Permet de changer le message de bienvenue sur un serveur discord !";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }
}
