package icu.skitdev.melen.managers;


import icu.skitdev.melen.Melen;
import icu.skitdev.melen.commands.fun.LoveCalcCommand;
import icu.skitdev.melen.commands.info.BotInfoCommand;
import icu.skitdev.melen.commands.info.ServerInfoCommand;
import icu.skitdev.melen.commands.info.UserInfoCommand;
import icu.skitdev.melen.commands.mod.BanCommand;
import icu.skitdev.melen.commands.mod.KickCommand;
import icu.skitdev.melen.commands.mod.MuteCommand;
import icu.skitdev.melen.commands.mod.UnmuteCommand;
import icu.skitdev.melen.commands.other.ShutdownCommand;
import icu.skitdev.melen.commands.utils.*;
import icu.skitdev.melen.objects.ICommand;
import icu.skitdev.melen.utils.useful.Constants;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {

    public static final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager() {
        addCommand(new ShutdownCommand());
        addCommand(new HelpCommand(this));
        addCommand(new PingCommand());
        addCommand(new LoveCalcCommand());
        addCommand(new BotInfoCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new SetChannelCommand());
        addCommand(new SuggestionCommand());
        addCommand(new SetWelcomeCommand());
        addCommand(new KickCommand());
        addCommand(new GithubCommand());
        addCommand(new UserInfoCommand());
        addCommand(new ServerInfoCommand());
        addCommand(new BanCommand());
        addCommand(new MuteCommand());
        addCommand(new UnmuteCommand());
        addCommand(new MessageCommand());
    }

    private void addCommand(ICommand command) {
        if (!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    public Collection<ICommand> getCommands() {
        return commands.values();
    }

    public ICommand getCommand(@NotNull String name) {
        return commands.get(name);
    }


    public void handleCommand(GuildMessageReceivedEvent event) {
        Document doc = Melen.database.getDocument("guilds",  "guildId",event.getGuild().getIdLong());
        String prefix = doc.getString("prefix") != null ? doc.getString("prefix") : Constants.PREFIX;
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();


        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            commands.get(invoke).handle(args, event);
        }
    }
}
