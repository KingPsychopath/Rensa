package com.edasaki.rensa.hangman.commands;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.commands.CommandMatchType;
import com.edasaki.rensa.hangman.HangmanManager;

import sx.blah.discord.handle.obj.IMessage;

public class HangmanGuessCommand extends AbstractCommand {

    public HangmanGuessCommand(String identifier, CommandMatchType type) {
        super(identifier, type);
    }

    @Override
    protected void execute(String[] args, IMessage msg) {
        if (!msg.getChannel().getName().contains("casino")) {
            Rensa.getInstance().sendResponse(msg, "come to the Casino chat to use Casino commands!");
            return;
        }
        if (!HangmanManager.inGame(msg)) {
            Rensa.getInstance().sendResponse(msg, "you aren't in a game of Hangman! Type `!hangman` to start playing.");
            return;
        }
        try {
            char guess = args[0].charAt(0);
            if (HangmanManager.guess(msg, guess)) {
                return;
            }
        } catch (Exception e) {
        }
        Rensa.getInstance().sendResponse(msg, "use the guessing command like this: `!hguess b`");
    }

    @Override
    public String getDescription() {
        return "Check if a letter is in the Hangman word.";
    }

}
