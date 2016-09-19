package com.edasaki.rensa.hangman;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.casino.CasinoManager;
import com.edasaki.rensa.logging.Saki;
import com.edasaki.rensa.utils.MessageCallback;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class HangmanManager extends AbstractManager {

    private static final int MAX_GUESSES = 6;

    private static ArrayList<String> words;

    private static HashMap<String, String> mapAnswer;
    private static HashMap<String, String> mapHidden;

    private static HashMap<String, Integer> mapGuessCount;
    private static HashMap<String, String> mapCurrentGuesses;

    private static HashMap<String, IMessage> mapLastStatusMessage;
    private static HashMap<String, IMessage> mapLastUpdateMessage;

    @Override
    public void initialize() {
        words = new ArrayList<String>();
        File dir = Rensa.getDataFolder("hangman");
        if (!dir.exists())
            dir.mkdirs();
        for (File f : dir.listFiles()) {
            try (Scanner scan = new Scanner(f)) {
                while (scan.hasNextLine()) {
                    words.add(scan.nextLine().trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mapAnswer = new HashMap<String, String>();
        mapHidden = new HashMap<String, String>();
        mapGuessCount = new HashMap<String, Integer>();
        mapCurrentGuesses = new HashMap<String, String>();
        mapLastStatusMessage = new HashMap<String, IMessage>();
        mapLastUpdateMessage = new HashMap<String, IMessage>();
        Saki.log("Registered " + words.size() + " words for Hangman.");
    }

    public static boolean startGame(IUser user) {
        if (mapAnswer.containsKey(user.getID()))
            return false; // already has a game in progress;
        String word = getRandomWord();
        mapAnswer.put(user.getID(), word);
        mapHidden.put(user.getID(), hideWord(word));
        return true;
    }

    private static void endGame(IMessage msg, boolean win) {
        IUser user = msg.getAuthor();
        String answer = mapAnswer.remove(user.getID());
        mapHidden.remove(user.getID());
        int guesses = mapGuessCount.remove(user.getID());
        mapCurrentGuesses.remove(user.getID());
        mapLastStatusMessage.remove(user.getID());
        mapLastUpdateMessage.remove(user.getID());
        if (win) {
            int length = answer.length();
            int max = (length - 5) * 50 + 300;
            int gained = (int) Math.ceil(max * (MAX_GUESSES - (double) guesses + 1) / (MAX_GUESSES + 1.0f));
            CasinoManager.giveZents(msg.getAuthor(), gained);
            Rensa.getInstance().sendResponse(msg, "congratulations, you win! :trophy::trophy::trophy:\nYour prize is " + gained + " " + CasinoManager.CURRENCY_NAME + ".\nYou now have " + CasinoManager.getZents(msg.getAuthor()) + " " + CasinoManager.CURRENCY_NAME + ".");
        } else {
            Rensa.getInstance().sendResponse(msg, "you lose. Better luck next time! The word was `" + answer + "`.\n```\n_____\n|  |\n|  X\n| -|-\n| / \\\n|____\n```\n");
        }
    }

    private static String hideWord(String word) {
        String dashed = word.replaceAll("[a-zA-Z]", "-");
        return dashed;
    }

    private static String getRandomWord() {
        return words.get((int) (Math.random() * words.size()));
    }

    public static boolean inGame(IMessage msg) {
        return mapAnswer.containsKey(msg.getAuthor().getID());
    }

    /**
     * 
     * @param msg
     * @param answer
     * @return if the command was successfully processed
     */
    public static boolean answer(IMessage msg, String answer) {
        if (!mapAnswer.containsKey(msg.getAuthor().getID()))
            return false;
        String ans = mapAnswer.get(msg.getAuthor().getID());
        boolean ret = answer.equalsIgnoreCase(ans);
        if (ret) {
            endGame(msg, true);
        } else {
            mapGuessCount.compute(msg.getAuthor().getID(), (k, v) -> {
                if (v != null)
                    return v + 1;
                return 1;
            });
            respond(msg, "that's not the word. Try again!");
            update(msg);
        }
        return true;
    }

    /**
     * 
     * @param msg
     * @param answer
     * @return if the command was successfully processed
     */
    public static boolean guess(IMessage msg, char guess) {
        String id = msg.getAuthor().getID();
        if (!mapAnswer.containsKey(id))
            return false;
        if (!Character.isLetter(guess)) {
            respond(msg, "you may only guess letters from `a` to `z`!");
            return false;
        }
        if (mapCurrentGuesses.getOrDefault(id, "").contains(guess + "")) {
            respond(msg, "you already guessed `" + guess + "`!");
            return true;
        }
        String ans = mapAnswer.get(id);
        String hidden = mapHidden.get(id);
        StringBuilder sb = new StringBuilder(hidden);
        int count = 0;
        for (int k = 0; k < ans.length(); k++) {
            if (ans.charAt(k) == guess) {
                sb.setCharAt(k, guess);
                count++;
            }
        }
        mapHidden.put(id, sb.toString());
        mapCurrentGuesses.put(id, mapCurrentGuesses.getOrDefault(id, "") + guess);
        if (count > 0) {
            respond(msg, "nice! `" + guess + "` appears " + count + " time" + (count == 1 ? "" : "s") + " in that word.");
            update(msg);
        } else {
            respond(msg, "too bad! The letter `" + guess + "` is not in the word.");
            mapGuessCount.compute(id, (k, v) -> {
                if (v != null)
                    return v + 1;
                return 1;
            });
            update(msg);
        }
        Rensa.getInstance().delete(msg);
        return true;
    }

    public static void update(IMessage msg) {
        update(msg, false);
    }

    public static void update(IMessage msg, boolean forceRedisplay) {
        final String id = msg.getAuthor().getID();
        int count = mapGuessCount.getOrDefault(msg.getAuthor().getID(), 0);
        if (count > MAX_GUESSES) {
            endGame(msg, false);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("here is your current hangman game:");
            sb.append('\n');
            sb.append("```");
            sb.append('\n');
            sb.append("______");
            sb.append('\n');
            sb.append("|");
            if (count >= 1)
                sb.append("  o");
            sb.append('\n');
            sb.append("| ");
            if (count >= 2) {
                if (count == 2)
                    sb.append(" |");
                else if (count == 3)
                    sb.append("-|");
                else
                    sb.append("-|-");
            }
            sb.append('\n');
            sb.append("| ");

            if (count >= 5) {
                if (count == 5)
                    sb.append("/");
                else
                    sb.append("/ \\");
            }
            sb.append('\n');
            sb.append("|_____");
            sb.append('\n');
            sb.append('\n');
            sb.append(mapHidden.getOrDefault(msg.getAuthor().getID(), "-ERROR-"));
            sb.append('\n');
            sb.append('\n');
            sb.append("You have ");
            sb.append(MAX_GUESSES - count);
            sb.append(" incorrect guess");
            if (MAX_GUESSES - count != 1)
                sb.append("es");
            sb.append(" left.");
            String guessed = mapCurrentGuesses.getOrDefault(msg.getAuthor().getID(), "").trim();
            if (guessed.length() > 0) {
                sb.append(" You have already guessed: ");
                sb.append(guessed);
            } else {
                sb.append(" You haven't guessed anything yet!");
            }
            sb.append("\nUse !hguess to guess a letter.\nUse !hanswer if you know the word!");
            sb.append("```");
            Rensa.getInstance().sendResponse(msg, sb.toString(), new MessageCallback() {
                @Override
                public void run(IMessage result) {
                    if (mapLastUpdateMessage.containsKey(id))
                        Rensa.getInstance().delete(mapLastUpdateMessage.remove(id));
                    mapLastUpdateMessage.put(id, result);
                }
            });
        }
    }

    private static void respond(IMessage msg, String s) {
        final String id = msg.getAuthor().getID();
        Rensa.getInstance().sendResponse(msg, s, new MessageCallback() {
            @Override
            public void run(IMessage result) {
                if (mapLastStatusMessage.containsKey(id))
                    Rensa.getInstance().delete(mapLastStatusMessage.remove(id));
                mapLastStatusMessage.put(id, result);
            }
        });
    }

}
