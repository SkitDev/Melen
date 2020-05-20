package icu.skitdev.melen.utils.useful;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class RecurrentAction {
    public static void noReason(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Merci d'indiquer une raison !").build()).queue();
    }

    public static void noUserMention(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Merci de mentionner un utilisateur !").build()).queue();
    }

    public static void noChannelMention(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Merci de mentionner un salon !").build()).queue();
    }

    public static void noChannel(GuildMessageReceivedEvent event, String type) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Le salon de " + type + "n'existe pas !").build()).queue();
    }

    public static String generateReason(List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            if (!arg.equals(args.get(0)))
                sb.append(arg).append(" ");
        }
        return sb.toString();
    }

    public static void botCannotInteract(GuildMessageReceivedEvent event, String action) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription(String.format("Je ne peux pas %s ce membre !", action)).build()).queue();
    }

    public static void userCannotInteract(GuildMessageReceivedEvent event, String action) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription(String.format("Tu ne peux pas %s ce membre !", action)).build()).queue();
    }

    public static void noPerm(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Tu ne possèdes pas la permission pour faire cela !").build()).queue();
    }

    public static void noSelfPerm(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Je ne possède pas la permission pour faire cela !").build()).queue();
    }

    public static void onCooldown(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Tu ne peux pas utiliser cette commande pour le moment !").build()).queue();
    }

    public static void noMessage(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Merci d'indiquer un message !").build()).queue();
    }

    public static void alreadyConnected(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Je suis déjà connecté à un salon vocal !").build()).queue();
    }

    public static void notInAVoiceChannel(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Tu dois être dans un salon vocal !").build()).queue();
    }

    public static void notANumber(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Merci d'indiquer un nombre !").build()).queue();
    }

    public static void sendPrivateChannel(User user, String content) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(content).queue());
    }

    public static void sendPrivateChannel(User user, EmbedBuilder content) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(content.build()).queue());
    }

    public static void notConnected(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Je ne suis pas connecté à un salon vocal !").build()).queue();
    }

    public static void notInTheSameChannel(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Tu dois être dans le même salon vocal que moi !").build()).queue();
    }

    public static void notALink(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(EmbedUtils.embedGenerator(event.getJDA(), "Erreur").setDescription("Merci d'indiquer un lien !").build()).queue();
    }
}
