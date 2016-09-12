package com.edasaki.rensa.input;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.logging.Saki;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

public class InputManager extends AbstractManager {

    private static InputThread inputThread;

    @Override
    public void initialize() {
        inputThread = new InputThread();
        inputThread.start();
    }

    public static void stop() {
        inputThread.terminate();
    }

    protected static void consoleCommand(String command) {
        if (!Rensa.isReady()) {
            Saki.error("Rensa is not fully loaded yet.");
            return;
        }
        command = command.trim();
        String[] args = command.split(" ");
        switch (args[0].toLowerCase()) {
            case "say":
                StringBuilder sb = new StringBuilder();
                for (int k = 2; k < args.length; k++) {
                    sb.append(args[k]);
                    sb.append(' ');
                }
                String channel = args[1];
                String message = sb.toString().trim();
                Rensa.getInstance().sendMessage(channel, message);
                break;
            case "stop":
            case "exit":
            case "quit":
                Saki.log("Rensa is shutting down...");
                try {
                    Rensa.getInstance().client.logout();
                } catch (RateLimitException | DiscordException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                break;
        }
    }

}
