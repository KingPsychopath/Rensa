package com.edasaki.rensa.chatterbot;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TimerTask;

public class SessionCleanupTask extends TimerTask {

    // The time in milliseconds it takes for a session to be considered inactive.
    private static final long INACTIVE = 60000;

    public void run() {
        ArrayList<String> toCleanup = new ArrayList<String>();
        for (Entry<String, Long> e : ChatterManager.lastActive.entrySet()) {
            if (System.currentTimeMillis() - e.getValue() > INACTIVE) {
                toCleanup.add(e.getKey());
            }
        }
        for (String s : toCleanup) {
            ChatterManager.sessions.remove(s);
            ChatterManager.lastActive.remove(s);
        }
    }
}