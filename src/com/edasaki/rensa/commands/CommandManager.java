package com.edasaki.rensa.commands;

import java.util.ArrayList;

import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.commands.misc.BullyCommand;
import com.edasaki.rensa.commands.misc.StatusCommand;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

public class CommandManager extends AbstractManager {

    private static ArrayList<AbstractCommand> commands = new ArrayList<AbstractCommand>();

    @Override
    public void initialize() {
        register(new BullyCommand("!bully", CommandMatchType.PREFIX));
        register(new StatusCommand("!status", CommandMatchType.PREFIX));
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String content = message.getContent();
        for (AbstractCommand ac : commands) {
            if (ac.check(content)) {
                ac.execute(content, message);
            }
        }
    }

    private static void register(AbstractCommand command) {
        commands.add(command);
    }

}
