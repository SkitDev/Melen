package icu.skitdev.melen.commands.utils;

import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.Constants;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.List;

public class SetChannelCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        if(!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)){
            RecurrentAction.noPerm(event);
            return;
        }
        if(args.size() < 2){
            Document doc = Melen.database.getDocument("guilds", "guildId", event.getGuild().getIdLong());
            String prefix = doc.getString("prefix") != null ? doc.getString("prefix") : Constants.PREFIX;
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Utilisation");
            eb.setDescription(String.format("```%s%s %s```", prefix, getInvoke(), getUsage()));
            eb.addField("Type :", "logs, suggestions, welcome", false);
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        String type = args.get(0).toLowerCase();
        if(event.getMessage().getMentionedChannels().isEmpty()){
            RecurrentAction.noChannelMention(event);
            return;
        }
        long channelId = event.getMessage().getMentionedChannels().get(0).getIdLong();
        EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Succès");
        switch(type){
            case "logs":
                update("logsId", channelId, event);
                eb.setDescription("Le nouveau salon des logs est : " + event.getMessage().getMentionedChannels().get(0).getAsMention());
                event.getChannel().sendMessage(eb.build()).queue();
                break;
            case "suggestions":
                update("suggestionsId", channelId, event);
                eb.setDescription("Le nouveau salon des suggestions est : " + event.getMessage().getMentionedChannels().get(0).getAsMention());
                event.getChannel().sendMessage(eb.build()).queue();
                break;
            case "welcome":
                update("welcomeId", channelId, event);
                eb.setDescription("Le nouveau salon de bienvenue est : " + event.getMessage().getMentionedChannels().get(0).getAsMention());
                event.getChannel().sendMessage(eb.build()).queue();
                break;
            default:
                EmbedBuilder ebr = EmbedUtils.embedGenerator(event.getJDA(), "Erreur");
                ebr.setDescription("Ce type de salon est invalide !");
                event.getChannel().sendMessage(ebr.build()).queue();
                break;
        }

    }

    @Override
    public String getUsage() {
        return "<type> <#salon>";
    }

    @Override
    public String getInvoke() {
        return "setchannel";
    }

    @Override
    public String getDescription() {
        return "Permet de définir les salons pour certaines fonctions du bot";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }

    private void update(String key, long channelId, GuildMessageReceivedEvent event){
        Document newDoc = new Document();
        newDoc.append(key, channelId);
        Melen.database.updateDocument("guilds","guildId", event.getGuild().getIdLong(), newDoc);

    }
}
