package icu.skitdev.melen.commands.utils;

import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import icu.skitdev.melen.enumerations.Category;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.Constants;
import icu.skitdev.melen.utils.useful.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class GithubCommand implements ICommand {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        String token = Constants.GITHUBTOKEN;
        if (args.isEmpty()) {
            event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Merci d'indiquer un pseudo github").build()).queue();
            return;
        }

        String name = args.get(0);
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get("https://api.github.com/users/" + name)
                    .header("Authorization", "token " + token)
                    .asJson();
            if (response.getStatus() != 200) {
                event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Ce pseudo github n'existe pas !").build()).queue();
                return;
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(response.getBody().toString());
            String prettyJsonString = gson.toJson(je);
            JsonObject jobj = gson.fromJson(prettyJsonString, JsonObject.class);
            EmbedBuilder eb = EmbedUtils.embedGenerator(event.getJDA(), "Info sur " + name + " :");
            System.out.println(prettyJsonString);
            if(jobj.has("login")) eb.addField("Pseudo :", jobj.get("login").getAsString(), false);
            if(jobj.has("blog") && !jobj.get("blog").getAsString().equalsIgnoreCase("")) eb.addField("Site :", jobj.get("blog").getAsString(), false);
            if(jobj.has("location")) eb.addField("Localisation :", jobj.get("location").getAsString(), false);
            if(jobj.has("bio")) eb.addField("Bio :", jobj.get("bio").getAsString(), false);
            if(jobj.has("followers")) eb.addField("Suivi par :", jobj.get("followers").getAsInt() + " personnes", false);
            if(jobj.has("html_url"))eb.addField("Profil GitHub :", jobj.get("html_url").getAsString(), false);
            if(jobj.has("public_repos")) eb.addField("Nombre de dépôts publics :", jobj.get("public_repos").getAsInt() + " dêpots", false);
            if(jobj.has("public_gists")) eb.addField("Nombre de gists publics :", jobj.get("public_gists").getAsInt() + " gists", false);
            if(jobj.has("avatar_url")) eb.setThumbnail(jobj.get("avatar_url").getAsString());

            event.getChannel().sendMessage(eb.build()).queue();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUsage() {
        return "<pseudo>";
    }

    @Override
    public String getInvoke() {
        return "github";
    }

    @Override
    public String getDescription() {
        return "Permet de récupérer des informations sur un profil Github";
    }

    @Override
    public Category getCategory() {
        return Category.UTILITAIRE;
    }
}
