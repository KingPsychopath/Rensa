package com.edasaki.rensa.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractCommand;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.casino.commands.BalanceCommand;
import com.edasaki.rensa.casino.commands.CoinFlipCommand;
import com.edasaki.rensa.casino.commands.GiftCommand;
import com.edasaki.rensa.casino.commands.LeaderboardCommand;
import com.edasaki.rensa.commands.misc.BullyCommand;
import com.edasaki.rensa.commands.misc.StatusCommand;
import com.edasaki.rensa.hangman.commands.HangmanAnswerCommand;
import com.edasaki.rensa.hangman.commands.HangmanCommand;
import com.edasaki.rensa.hangman.commands.HangmanGuessCommand;
import com.edasaki.rensa.music.commands.PlayMusicCommand;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class CommandManager extends AbstractManager {

    private static LinkedHashMap<String, ArrayList<AbstractCommand>> commands = new LinkedHashMap<String, ArrayList<AbstractCommand>>();

    private static int longestID = 10;

    @Override
    public void initialize() {
        register("Casino", new BalanceCommand("!bal", CommandMatchType.PREFIX));
        register("Casino", new GiftCommand("!gift", CommandMatchType.PREFIX));
        register("Casino", new CoinFlipCommand("!coinflip", CommandMatchType.PREFIX));
        register("Casino", new LeaderboardCommand("!leaderboard", CommandMatchType.PREFIX));
        register("Casino - Hangman", new HangmanCommand("!hangman", CommandMatchType.PREFIX));
        register("Casino - Hangman", new HangmanGuessCommand("!hguess", CommandMatchType.PREFIX));
        register("Casino - Hangman", new HangmanAnswerCommand("!hanswer", CommandMatchType.PREFIX));
        register("Music", new PlayMusicCommand("!play", CommandMatchType.PREFIX));
        register("Miscellaneous", new StatusCommand("!status", CommandMatchType.PREFIX));
        register("Miscellaneous", new BullyCommand("!bully", CommandMatchType.PREFIX));
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        IMessage message = event.getMessage();
        String content = message.getContent();
        if (content.trim().matches(".help")) {
            sendHelp(message.getAuthor());
            Rensa.getInstance().sendMessage(message.getChannel(), message.getAuthor().mention() + ", I've sent you a PM with info about me! :wink:");
        } else {
            for (ArrayList<AbstractCommand> list : commands.values()) {
                for (AbstractCommand ac : list) {
                    if (ac.check(content)) {
                        ac.execute(content, message);
                    }
                }
            }
        }
    }

    private void sendHelp(IUser user) {
        StringBuilder sb = new StringBuilder();
        sb.append("```diff");
        sb.append('\n');
        sb.append("! Rensa's Commands");
        sb.append('\n');
        for (Entry<String, ArrayList<AbstractCommand>> e : commands.entrySet()) {
            sb.append('\n');
            sb.append("- ");
            sb.append(e.getKey());
            sb.append(':');
            sb.append('\n');
            for (AbstractCommand ac : e.getValue()) {
                sb.append("+ ");
                String formatted = String.format("%-" + longestID + "s %s", ac.getIdentifier(), ac.getDescription());
                sb.append(formatted);
                sb.append('\n');
            }
        }
        sb.append('\n');
        sb.append("Rensa was written by Edasaki and is released open-source under the MIT License.\n");
        sb.append("Check out the project! https://github.com/edasaki/Rensa");
        sb.append("```");
        try {
            user.getOrCreatePMChannel().sendMessage(sb.toString());
        } catch (MissingPermissionsException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
    }

    private static void register(String category, AbstractCommand command) {
        if (!commands.containsKey(category))
            commands.put(category, new ArrayList<AbstractCommand>());
        commands.get(category).add(command);
        if (command.getIdentifier().length() + 3 > longestID)
            longestID = command.getIdentifier().length() + 3;
    }

}
