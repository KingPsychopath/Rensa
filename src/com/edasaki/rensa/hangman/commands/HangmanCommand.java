package com.edasaki.rensa.hangman.commands;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.commands.CommandMatchType;
import com.edasaki.rensa.hangman.HangmanManager;

import sx.blah.discord.handle.obj.IMessage;

public class HangmanCommand extends AbstractCommand {

    public HangmanCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        if (!msg.getChannel().getName().contains("casino")) {
            Rensa.getInstance().sendResponse(msg, "come to the Casino chat to use Casino commands!");
            return;
        }
        if (HangmanManager.startGame(msg.getAuthor())) {
            Rensa.getInstance().sendResponse(msg, "I've started a new game of Hangman for you. Good luck!");
            HangmanManager.update(msg);
        } else {
            Rensa.getInstance().sendResponse(msg, "you've already started a game of Hangman!");
            HangmanManager.update(msg);
        }
    }

    @Override
    public String getDescription() {
        return "Start a game of Hangman.";
    }

}
