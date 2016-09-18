package com.edasaki.rensa.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.logging.Saki;

public class ConfigManager extends AbstractManager {

    private static HashMap<Config, String> configs = new HashMap<Config, String>();

    public static String getToken() {
        return getConfig(Config.BOT_TOKEN);
    }

    public static String getPrimaryGuild() {
        return getConfig(Config.GUILD_ID);
    }

    public static String getMusicChannel() {
        return getConfig(Config.MUSIC_CHANNEL);
    }

    private static String getConfig(Config config) {
        if (configs.containsKey(config)) {
            return configs.get(config);
        }
        Saki.error("Rensa is missing a required configuration!");
        Saki.error("Please input a value for: " + config.getDisplayName());
        // System.in should NOT be closed.
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        String input = null;
        while (input == null || input.length() == 0) {
            input = scan.nextLine();
        }
        Saki.db("read " + input);
        configs.put(config, input);
        Saki.error("Configuration updated. You may edit the configuration at config.txt if there was a mistake.");
        save();
        return input;
    }

    private static void save() {
        File cfg = new File("config.txt");
        if (!cfg.exists()) {
            try {
                cfg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(cfg)))) {
            for (Entry<Config, String> e : configs.entrySet()) {
                out.println(e.getKey().toString() + ":" + e.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() {
        File cfg = new File("config.txt");
        if (!cfg.exists()) {
            try {
                cfg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (Scanner scan = new Scanner(cfg)) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                try {
                    Config config = Config.valueOf(line.substring(0, line.indexOf(':')).trim());
                    String value = line.substring(line.indexOf(':') + 1).trim();
                    configs.put(config, value);
                    Saki.log("Read config: " + config + ":" + value);
                } catch (Exception e) {
                    Saki.error("Invalid config: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Will require all configs to be filled in before run
        for (Config c : Config.values()) {
            getConfig(c);
        }
    }

}
