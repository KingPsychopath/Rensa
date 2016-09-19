package com.edasaki.rensa.casino.commands;

import java.util.HashMap;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.casino.CasinoManager;
import com.edasaki.rensa.commands.CommandMatchType;
import com.edasaki.rensa.utils.SakiUtils;

import sx.blah.discord.handle.obj.IMessage;

public class GiftCommand extends AbstractCommand {

    private static final long DELAY = 10 * 60 * 1000;
    private static final String DELAY_STRING = "10 minutes";

    private HashMap<String, Long> lastGift = new HashMap<String, Long>();

    public GiftCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        if (!msg.getChannel().getName().contains("casino")) {
            Rensa.getInstance().sendResponse(msg, "come to the Casino chat to use Casino commands!");
            return;
        }
        String id = msg.getAuthor().getID();
        if (lastGift.containsKey(id) && System.currentTimeMillis() - lastGift.get(id) < DELAY) {
            Rensa.getInstance().sendResponse(msg, "you can only get a gift once every " + DELAY_STRING + ". Come back in " + SakiUtils.formatMillis(DELAY - (System.currentTimeMillis() - lastGift.get(id))) + "!");
        } else {
            double rand = Math.random();
            int amt = 100;
            String message = "here's 100 free " + CasinoManager.CURRENCY_NAME + " for you!";
            if (rand < 0.01) {
                amt = 500;
                message = "it's your lucky day! I'll give you 500 " + CasinoManager.CURRENCY_NAME + " this time!";
            } else if (rand < 0.06) {
                amt = 180;
                message = "I'm feeling generous. Here's 180 " + CasinoManager.CURRENCY_NAME + " for you!";
            } else if (rand < 0.15) {
                amt = 70;
                message = "sorry but I'm running a little low on " + CasinoManager.CURRENCY_NAME + " right now. I only have 70 for you.";
            } else if (rand < 0.20) {
                amt = 120;
                message = "here's 120 " + CasinoManager.CURRENCY_NAME + "! I threw in a little extra because we're such good friends.";
            }
            CasinoManager.giveZents(msg.getAuthor(), amt);
            Rensa.getInstance().sendResponse(msg, message + "\nYou now have " + CasinoManager.getZents(msg.getAuthor()) + " " + CasinoManager.CURRENCY_NAME + ".");
            lastGift.put(id, System.currentTimeMillis());
        }
    }

    @Override
    public String getDescription() {
        return "Receive a small amount of  " + CasinoManager.CURRENCY_NAME + " for free every " + DELAY_STRING + ".";
    }

}
