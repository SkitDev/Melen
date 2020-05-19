package icu.skitdev.melen.commands.utils;

import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.managers.CommandManager;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.Constants;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.List;

public class HelpCommand implements ICommand {
    CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Document doc = Melen.database.getDocument("guilds", "guildId", event.getGuild().getIdLong());
        String prefix = doc.getString("prefix") != null ? doc.getString("prefix") : Constants.PREFIX;

        if (args.isEmpty()) {
            generateAndSendEmbed(event, commandManager, prefix);
            return;
        }

        ICommand command = commandManager.getCommand(String.join("", args));

        if (command == null) {
            sendNotCommand(event, String.join("", args), commandManager, prefix);
            return;
        }

        EmbedBuilder ebd = EmbedUtils.embedGenerator(event.getJDA(), "Info sur la commande " + command.getInvoke());
        ebd.addField("Description :", command.getDescription(), false);
        ebd.addField("Utilisation :", String.format("```%s%s %s```", prefix, command.getInvoke(), command.getUsage()), false);
        ebd.addField("Catégorie :", command.getCategory().getName(), false);
        event.getChannel().sendMessage(ebd.build()).queue();

    }

    @Override
    public String getUsage() {
        return "[commande]";
    }

    @Override
    public String getInvoke() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Voir les commandes du bot";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }

    private static void sendNotCommand(GuildMessageReceivedEvent event, String command, CommandManager manager, String prefix) {
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Commande inconnue");
        eb.addField("La commande **" + command + "** n'existe pas !", "Utilise `" + prefix + new HelpCommand(manager).getInvoke() + "` pour une liste des commandes.", false);

        event.getChannel().sendMessage(eb.build()).queue();
    }

    private static void generateAndSendEmbed(GuildMessageReceivedEvent event, CommandManager manager, String prefix) {

        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "La liste de toutes mes commandes");
        StringBuilder utils = new StringBuilder();
        StringBuilder info = new StringBuilder();
        StringBuilder mod = new StringBuilder();
        StringBuilder fun = new StringBuilder();
        StringBuilder other = new StringBuilder();
manager.getCommands().forEach(
                (command) -> {
                    if (command.getCategory() == Category.UTILITAIRE) {
                        utils.append("**").append(prefix).append(command.getInvoke()).append("** : *").append(command.getDescription()).append("*\n");
                    }
                    if (command.getCategory() == Category.INFORMATIVE) {
                        info.append("**").append(prefix).append(command.getInvoke()).append("** : *").append(command.getDescription()).append("*\n");
                    }
                    if (command.getCategory() == Category.MODERATION){
                        mod.append("**").append(prefix).append(command.getInvoke()).append("** : *").append(command.getDescription()).append("*\n");

                    }
                    if (command.getCategory() == Category.FUN){
                        fun.append("**").append(prefix).append(command.getInvoke()).append("** : *").append(command.getDescription()).append("*\n");

                    }
                    if (command.getCategory() == Category.AUTRE) {
                        other.append("**").append(prefix).append(command.getInvoke()).append("** : *").append(command.getDescription()).append("*\n");
                    }
                });
        if (!utils.toString().isEmpty())
            eb.addField("Utilitaire :", utils.toString(), false);
        if (!info.toString().isEmpty())
            eb.addField("Informative :", info.toString(), false);
        if (!fun.toString().isEmpty())
            eb.addField("Fun :", fun.toString(), false);
        if (!mod.toString().isEmpty())
            eb.addField("Modération :", mod.toString(), false);
        if (!other.toString().isEmpty())
            eb.addField("Autre :", other.toString(), false);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
