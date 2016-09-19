package com.edasaki.rensa.casino;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.abstracts.AbstractManager;
import com.edasaki.rensa.logging.Saki;

import sx.blah.discord.handle.obj.IUser;

public class CasinoManager extends AbstractManager {

    public static final String CURRENCY_NAME = "Zents";

    private static HashMap<String, Integer> zents = new HashMap<String, Integer>();

    private static long lastLeaderboard = 0;
    private static List<Entry<String, Integer>> cachedLeaderboard = null;

    private static File getDataFile() {
        File dir = Rensa.getDataFolder("casino");
        File dataFile = new File(dir, "data.txt");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataFile;
    }

    @Override
    public void initialize() {
        try (Scanner scan = new Scanner(getDataFile())) {
            while (scan.hasNextLine()) {
                try {
                    String[] data = scan.nextLine().trim().split(" ");
                    zents.put(data[0], Integer.parseInt(data[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Saki.log("Loaded Casino data for " + zents.size() + " users.");
    }

    private static void save() {
        new Thread(() -> saveSync()).start();
    }

    private static synchronized void saveSync() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(getDataFile())))) {
            for (Entry<String, Integer> e : zents.entrySet()) {
                out.println(e.getKey() + " " + e.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Entry<String, Integer>> getLeaderboard() {
        if (System.currentTimeMillis() - lastLeaderboard > 30000 || cachedLeaderboard == null) {
            Set<Entry<String, Integer>> set = zents.entrySet();
            List<Entry<String, Integer>> keys = new ArrayList<Entry<String, Integer>>(set);
            Collections.sort(keys, new Comparator<Entry<String, Integer>>() {
                @Override
                public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
                    return Integer.compare(e2.getValue(), e1.getValue());
                }
            });
            cachedLeaderboard = keys.subList(0, 10 > keys.size() ? keys.size() : 10);
        }
        return cachedLeaderboard;
    }

    public static int getZents(IUser user) {
        if (zents.containsKey(user.getID()))
            return zents.get(user.getID());
        zents.put(user.getID(), 0);
        save();
        return 0;
    }

    public static void giveZents(IUser user, int amt) {
        int total = getZents(user) + amt;
        zents.put(user.getID(), total);
        save();
    }

}
