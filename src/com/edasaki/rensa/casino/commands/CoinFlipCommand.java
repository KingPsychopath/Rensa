package com.edasaki.rensa.casino.commands;

import java.util.HashMap;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.casino.CasinoManager;
import com.edasaki.rensa.commands.CommandMatchType;
import com.edasaki.rensa.utils.MessageCallback;

import sx.blah.discord.handle.obj.IMessage;

public class CoinFlipCommand extends AbstractCommand {

    private HashMap<String, IMessage> lastMessage = new HashMap<String, IMessage>();
    private HashMap<String, IMessage> lastResult = new HashMap<String, IMessage>();

    public CoinFlipCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        if (!msg.getChannel().getName().contains("casino")) {
            Rensa.getInstance().sendResponse(msg, "come to the Casino chat to use Casino commands!");
            return;
        }
        String id = msg.getAuthor().getID();
        if (lastMessage.containsKey(id))
            Rensa.getInstance().delete(lastMessage.remove(id));
        lastMessage.put(id, msg);
        try {
            int bet = Integer.parseInt(args[0]);
            int curr = CasinoManager.getZents(msg.getAuthor());
            if (bet < 1) {
                respond(msg, "that's not a valid bet!");
            } else if (bet > curr) {
                respond(msg, "you don't have " + bet + " " + CasinoManager.CURRENCY_NAME + "!");
            } else {
                final boolean win = Math.random() < 0.5; // Rensa supports fair, unrigged gambling!
                if (win) {
                    CasinoManager.giveZents(msg.getAuthor(), bet);
                    respond(msg, "you won! Congratulations!\nYou now have " + CasinoManager.getZents(msg.getAuthor()) + " " + CasinoManager.CURRENCY_NAME + ".");
                } else {
                    CasinoManager.giveZents(msg.getAuthor(), -bet);
                    respond(msg, "you lost! Too bad.\nYou now have " + CasinoManager.getZents(msg.getAuthor()) + " " + CasinoManager.CURRENCY_NAME + ".");
                }
            }
        } catch (Exception e) {
            Rensa.getInstance().sendResponse(msg, "if you'd like to play Coin Flip, please use the command like this: `!coinflip <bet>`");
        }
    }

    private void respond(IMessage message, String s) {
        Rensa.getInstance().sendResponse(message, s, new MessageCallback() {
            @Override
            public void run(IMessage result) {
                String id = message.getAuthor().getID();
                if (lastResult.containsKey(id))
                    Rensa.getInstance().delete(lastResult.remove(id));
                lastResult.put(id, result);
            }
        });
    }

    @Override
    public String getDescription() {
        return "Flip a fair coin! 50% chance to double your bet, 50% chance to lose it all!";
    }

}
