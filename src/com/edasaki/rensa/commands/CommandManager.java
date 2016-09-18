package com.edasaki.rensa.commands;

import java.util.ArrayList;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.casino.comands.BalanceCommand;
import com.edasaki.rensa.commands.misc.BullyCommand;
import com.edasaki.rensa.commands.misc.StatusCommand;
import com.edasaki.rensa.music.commands.PlayMusicCommand;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class CommandManager extends AbstractManager {

    private static ArrayList<AbstractCommand> commands = new ArrayList<AbstractCommand>();

    @Override
    public void initialize() {
        register(new BullyCommand("!bully", CommandMatchType.PREFIX));
        register(new StatusCommand("!status", CommandMatchType.PREFIX));
        register(new BalanceCommand("!balance", CommandMatchType.PREFIX));
        register(new PlayMusicCommand("!play", CommandMatchType.PREFIX));
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String content = message.getContent();
        if (content.trim().matches(".help")) {
            sendHelp(message.getAuthor());
            Rensa.getInstance().sendMessage(message.getChannel(), message.getAuthor().mention() + ", I've sent you a PM with info about me! :wink:");
        } else {
            for (AbstractCommand ac : commands) {
                if (ac.check(content)) {
                    ac.execute(content, message);
                }
            }
        }
    }

    private void sendHelp(IUser user) {
        String help = "details coming soon..";
        try {
            user.getOrCreatePMChannel().sendMessage(help);
        } catch (MissingPermissionsException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    public static void register(AbstractCommand command) {
        commands.add(command);
    }

}
