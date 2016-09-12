package com.edasaki.rensa;

import com.edasaki.rensa.config.ConfigManager;
import com.edasaki.rensa.logging.Saki;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

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

    public void sendMessage(String channel, String message) {
        for (IChannel ch : client.getGuildByID(ConfigManager.getPrimaryGuild()).getChannels()) {
            if (ch.getName().equalsIgnoreCase(channel)) {
                sendMessage(ch, message);
            }
        }
    }

    public void sendMessage(IChannel ch, String message) {
        try {
            ch.sendMessage(message);
        } catch (RateLimitException | MissingPermissionsException | DiscordException e) {
            e.printStackTrace();
        }
    }

    @EventSubscriber
    public void onReady(ReadyEvent event) {
        ready = true;
        Saki.log("Successfully loaded Rensa!");
    }

    public void registerListener(Object listener) {
        client.getDispatcher().registerListener(listener);
        Saki.log("Registered listener " + listener.getClass().getName() + ".");
    }

    public static Rensa getInstance() {
        return instance;
    }

    public static void setInstance(Rensa instance) {
        Rensa.instance = instance;
    }

}
