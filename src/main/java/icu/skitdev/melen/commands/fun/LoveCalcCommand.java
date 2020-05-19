package icu.skitdev.melen.commands.fun;

import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Random;

public class LoveCalcCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if (args.size() < 2){
            event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Merci d'indiquer le nom de 2 personnes !").build()).queue();
            return;
        }
        String name1 = args.get(0);
        String name2 = args.get(1);

        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "L'amour de " + name1 + " et "+ name2 +" :heart:");
        int lovePercentage = new Random().nextInt(100);
        if(lovePercentage < 20){
            eb.setDescription("Il semble que " + name1 + " et " + name2 + " ne soient pas compatible ! (" + lovePercentage + "%)");
        }else if(lovePercentage < 50){
            eb.setDescription("Il semble que " + name1 + " et " + name2 + " soient très différents et leur relation risque d'être courte ! (" + lovePercentage + "%)");
        } else if(lovePercentage < 75){
            eb.setDescription("Il semble que " + name1 + " et " + name2 + " se ressemblent suffisamment pour pouvoir avoir une relation durable ensemble ! (" + lovePercentage + "%)");
        }else {
            eb.setDescription("Il semble que " + name1 + " et " + name2 + " soient fait pour être ensemble !\n Leur relation sera magnifique ! (" + lovePercentage + "%)");
        }

        event.getChannel().sendMessage(eb.build()).queue();

    }

    @Override
    public String getUsage() {
        return "<nom> <nom>";
    }

    @Override
    public String getInvoke() {
        return "lovecalc";
    }

    @Override
    public String getDescription() {
        return "Calcule le pourcentage d'amour entre deux personne";
    }

    @Override
    public Category getCategory() {
        return Category.FUN;
    }
}
