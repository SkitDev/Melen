package icu.skitdev.melen.commands.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import icu.skitdev.melen.Melen;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.Databases;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import icu.skitdev.melen.utils.useful.RecurrentAction;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MessageCommand implements ICommand {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        Databases database = Melen.database;
        Document doc;
        if (database.isExists("messagesCooldown", "userId", event.getAuthor().getIdLong())) {
            doc = database.getDocument("messagesCooldown", "userId", event.getAuthor().getIdLong());
            if(doc.containsKey("timestamp")){
                Date date = doc.getDate("timestamp");
                Date now = new Date();

                if (!now.after(date) && !now.equals(date)) {
                    RecurrentAction.onCooldown(event);
                    return;
                }
            }
        } else {
            database.insertDocument("messagesCooldown", new Document("userId", event.getAuthor().getIdLong()));
            doc = database.getDocument("messagesCooldown", "userId", event.getAuthor().getIdLong());
        }
        if (args.size() == 0) {
            RecurrentAction.noMessage(event);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String msg = sb.toString();

        HttpResponse<String> response;
        try {
            response = Unirest.post("https://maker.ifttt.com/trigger/melen_message/with/key/cdjrpVFnXNw8XIThf5fNZCtD_ZmqUxKv2D4IYwOt1Xm")
                    .field("value1", event.getAuthor().getAsTag())
                    .field("value2", msg)
                    .asString();
            if (response.getStatus() != 200) {
                event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Impossible d'envoyer le message au créateur du bot !").build()).queue();
                return;
            }
            event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Succès").setDescription("Message envoyé !").build()).queue();
            doc.remove("timestamp");
            doc.append("timestamp", new Timestamp(System.currentTimeMillis()).toLocalDateTime().plusDays(1));
            database.updateDocument("messagesCooldown", "userId", event.getAuthor().getIdLong(), doc);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getUsage() {
        return "<message>";
    }

    @Override
    public String getInvoke() {
        return "message";
    }

    @Override
    public String getDescription() {
        return "Permet d'envoyer un message au créateur du bot";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }
}
