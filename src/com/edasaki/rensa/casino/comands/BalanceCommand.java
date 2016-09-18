package com.edasaki.rensa.casino.comands;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.casino.CasinoManager;
import com.edasaki.rensa.commands.CommandMatchType;

import sx.blah.discord.handle.obj.IMessage;

public class BalanceCommand extends AbstractCommand {

    public BalanceCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        String response = msg.getAuthor().mention() + ", your balance is " + CasinoManager.getZents(msg.getAuthor()) + " " + CasinoManager.CURRENCY_NAME + ".";
        Rensa.getInstance().sendMessage(msg.getChannel(), response);
    }

}
