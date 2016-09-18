package com.edasaki.rensa.chatterbot;

import java.util.HashMap;
import java.util.Timer;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MentionEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IDiscordObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.MessageTokenizer;
import sx.blah.discord.util.MessageTokenizer.MentionToken;

@SuppressWarnings("unused")
public class ChatterManager extends AbstractManager {

    protected static HashMap<String, ChatterBotSession> sessions = new HashMap<String, ChatterBotSession>();
    protected static HashMap<String, Long> lastActive = new HashMap<String, Long>();

    private static final ChatterBotFactory FACTORY = new ChatterBotFactory();

    private static final ChatterBot CLEVERBOT;
    static {
        ChatterBot cb = null;
        try {
            cb = FACTORY.create(ChatterBotType.CLEVERBOT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CLEVERBOT = cb;
    }

    private static final ChatterBot PANDORABOT;
    static {
        ChatterBot cb = null;
        try {
            cb = FACTORY.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477"); // Key given by ChatterBot API
        } catch (Exception e) {
            e.printStackTrace();
        }
        PANDORABOT = cb;
    }

    @Override
    public void initialize() {
        Timer time = new Timer();
        SessionCleanupTask st = new SessionCleanupTask();
        time.schedule(st, 0, 180000);
    }

    private ChatterBotSession getNewSession() {
        try {
            //            ChatterBotSession session = (Math.random() < 0.5 ? PANDORABOT : CLEVERBOT).createSession();
            ChatterBotSession session = PANDORABOT.createSession(); // Pandora seems a little more "human-like" in my opinion.
            //            ChatterBotSession session = CLEVERBOT.createSession();
            return session;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void respond(String input, String sender, IUser respondTo, IChannel channel) {
        lastActive.put(sender, System.currentTimeMillis());
        if (!sessions.containsKey(sender)) {
            sessions.put(sender, getNewSession());
        }
        ChatterBotSession session = sessions.get(sender);
        try {
            String response = respondTo.mention() + " " + session.think(input);
            if (response.contains("<a href"))
                response = "I don't understand.";
            Rensa.getInstance().sendMessage(channel, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventSubscriber
    public void onMention(MentionEvent event) {
        IMessage message = event.getMessage();
        IGuild guild = message.getGuild();
        String content = message.getContent();
        MessageTokenizer tokenizer = new MessageTokenizer(Rensa.getInstance().client, content);
        while (tokenizer.hasNextMention()) {
            MentionToken<?> token = tokenizer.nextMention();
            IDiscordObject<?> obj = token.getMentionObject();
            String replaceWith = "null";
            if (obj == Rensa.getInstance().client.getOurUser()) {
                replaceWith = "";
            } else if (obj instanceof IUser) {
                replaceWith = ((IUser) obj).getDisplayName(guild);
            } else if (obj instanceof IRole) {
                replaceWith = ((IRole) obj).getName();
            } else if (obj instanceof IChannel) {
                replaceWith = ((IChannel) obj).getName();
            }
            int start = token.getStartIndex();
            int end = token.getEndIndex();
            String pre = content.substring(0, start).trim();
            String post = content.substring(end).trim();
            replaceWith = replaceWith.trim();
            content = pre + " " + replaceWith + " " + post; // there seems to be a space added after all token contents
            tokenizer = new MessageTokenizer(Rensa.getInstance().client, content);
        }
        respond(content, message.getAuthor().getID(), message.getAuthor(), message.getChannel());
    }

}
