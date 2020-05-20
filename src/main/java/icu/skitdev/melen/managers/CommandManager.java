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
import icu.skitdev.melen.commands.music.*;
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
        //fun
        addCommand(new LoveCalcCommand());

        //info
        addCommand(new BotInfoCommand());
        addCommand(new ServerInfoCommand());
        addCommand(new UserInfoCommand());

        //moderation
        addCommand(new BanCommand());
        addCommand(new KickCommand());
        addCommand(new MuteCommand());
        addCommand(new UnmuteCommand());

        //music
        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new PlayCommand());
        addCommand(new QueueCommand());
        addCommand(new SkipCommand());
        addCommand(new StopCommand());

        //other
        addCommand(new ShutdownCommand());

        //utils
        addCommand(new GithubCommand());
        addCommand(new HelpCommand(this));
        addCommand(new MessageCommand());
        addCommand(new PingCommand());
        addCommand(new SetChannelCommand());
        addCommand(new SetPrefixCommand());
        addCommand(new SetWelcomeCommand());
        addCommand(new SuggestionCommand());


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
