package com.edasaki.rensa;

import org.slf4j.LoggerFactory;

import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.casino.CasinoManager;
import com.edasaki.rensa.chatterbot.ChatterManager;
import com.edasaki.rensa.commands.CommandManager;
import com.edasaki.rensa.config.ConfigManager;
import com.edasaki.rensa.hangman.HangmanManager;
import com.edasaki.rensa.input.InputManager;
import com.edasaki.rensa.logging.Saki;
import com.edasaki.rensa.music.MusicManager;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class RensaLauncher {

    public static void main(String[] args) {
        Saki.log("Loading Rensa...");

        // Disable Discord4J built-in debug messages (it's super spammy)
        Logger root = (Logger) LoggerFactory.getLogger(Discord4J.class);
        root.setLevel(Level.INFO);

        // ConfigManager must be created first for getClientInstance()
        AbstractManager.create(ConfigManager.class, true);

        IDiscordClient client = getClientInstance();
        Rensa.setInstance(new Rensa(client));

        AbstractManager.create(InputManager.class);
        AbstractManager.create(CommandManager.class);
        AbstractManager.create(ChatterManager.class);
        AbstractManager.create(CasinoManager.class);
        AbstractManager.create(HangmanManager.class);
        AbstractManager.create(MusicManager.class);

        Saki.log("Preparing connection...");

    }

    public static IDiscordClient getClientInstance() {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(ConfigManager.getToken());
        try {
            return clientBuilder.login();
        } catch (DiscordException e) {
            e.printStackTrace();
        }
        return null;
    }
}
