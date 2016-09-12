package com.edasaki.rensa.commands.misc;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.commands.CommandMatchType;

import sx.blah.discord.handle.obj.IMessage;

public class BullyCommand extends AbstractCommand {

    public BullyCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        Rensa.getInstance().sendMessage(msg.getChannel(), "Hey! No bullying! <http://twitter.com/antibullyranger>");
    }

}
