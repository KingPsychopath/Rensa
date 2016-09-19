package com.edasaki.rensa.casino.commands;

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
        if (!msg.getChannel().getName().contains("casino")) {
            Rensa.getInstance().sendResponse(msg, "come to the Casino chat to use Casino commands!");
            return;
        }
        Rensa.getInstance().sendResponse(msg, "your balance is " + CasinoManager.getZents(msg.getAuthor()) + " " + CasinoManager.CURRENCY_NAME + ".");
    }

    @Override
    public String getDescription() {
        return "See your account balance.";
    }

}
