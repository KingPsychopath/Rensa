package com.edasaki.rensa;

import java.io.File;

import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.config.ConfigManager;
import com.edasaki.rensa.logging.Saki;
import com.edasaki.rensa.utils.MessageCallback;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

public class Rensa {

    private static Rensa instance;
    private static boolean ready = false;

    public IDiscordClient client;

    public Rensa(IDiscordClient client) {
        this.client = client;
        registerListener(this);
    }

    public static boolean isReady() {
        boolean val = instance != null && instance.client != null && ready;
        return val;
    }

    public static File getDataFolder() {
        File f = new File("data" + File.separator);
        if (!f.exists())
            f.mkdirs();
        return f;
    }

    public static File getDataFolder(String subdir) {
        File f = new File("data" + File.separator, subdir);
        if (!f.exists())
            f.mkdirs();
        return f;
    }

    public void sendMessage(String channel, String message) {
        sendMessage(client.getGuildByID(ConfigManager.getPrimaryGuild()), channel, message);
    }

    public void sendMessage(IGuild guild, String channel, String message) {
        for (IChannel ch : guild.getChannels()) {
            if (ch.getName().equalsIgnoreCase(channel)) {
                sendMessage(ch, message);
            }
        }
    }

    public void sendMessage(IChannel ch, String message) {
        sendMessage(ch, message, null);
    }

    public void sendMessage(IChannel ch, String message, MessageCallback callback) {
        RequestBuffer.request(() -> {
            try {
                IMessage result = ch.sendMessage(message);
                if (callback != null)
                    callback.run(result);
            } catch (MissingPermissionsException | DiscordException e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(IMessage message) {
        RequestBuffer.request(() -> {
            try {
                message.delete();
            } catch (MissingPermissionsException | DiscordException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendResponse(IMessage msg, String message) {
        sendResponse(msg, message, null);
    }

    public void sendResponse(IMessage msg, String message, MessageCallback callback) {
        String response = msg.getAuthor().mention() + ", " + message;
        sendMessage(msg.getChannel(), response, callback);
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        ready = true;
        AbstractManager.processQueuedManagers();
        AbstractManager.registerAll();
        Saki.log("Successfully loaded Rensa!");
    }

    public void registerListener(Object listener) {
        client.getDispatcher().registerListener(listener);
        Saki.log("Registered listener " + listener.getClass().getName() + ".");
    }

    public static Rensa instance() {
        return instance;
    }

    public static Rensa getInstance() {
        return instance;
    }

    public static void setInstance(Rensa instance) {
        Rensa.instance = instance;
    }

}
