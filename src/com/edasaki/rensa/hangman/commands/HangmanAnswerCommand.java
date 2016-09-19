package com.edasaki.rensa.hangman.commands;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.commands.CommandMatchType;
import com.edasaki.rensa.hangman.HangmanManager;

import sx.blah.discord.handle.obj.IMessage;

public class HangmanAnswerCommand extends AbstractCommand {

    public HangmanAnswerCommand(String identifier, CommandMatchType type) {
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
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < args.length; k++) {
                sb.append(args[k]);
                sb.append(' ');
            }
            if (HangmanManager.answer(msg, sb.toString().trim())) {
                return;
            }
        } catch (Exception e) {
        }
        Rensa.getInstance().sendResponse(msg, "use the answering command like this: `!hguess butterfly`");
    }

    @Override
    public String getDescription() {
        return "Guess the entire Hangman word. Win a prize if you get it right!";
    }

}
