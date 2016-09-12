package com.edasaki.rensa.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The remnants of the old SakiBot.
 * This class is used for logging info and error messages.
 * @author Edasaki
 */

public class Saki {

    private static final SimpleDateFormat TIMESTAMP = new SimpleDateFormat("HH:mm:ss");

    private static String getTimestamp() {
        return TIMESTAMP.format(new Date());
    }

    /**
     * Log an error.
     * @param message the message to log
     */
    public static void error(String message) {
        System.out.println("[SAKI|Rensa] " + getTimestamp() + " ERROR: " + message);
    }

    /**
     * Log information.
     * @param message the message to log
     */
    public static void info(String message) {
        System.out.println("[SAKI|Rensa] " + getTimestamp() + " INFO: " + message);
    }

    /**
     * Alias for info(String).
     * @param message
     */
    public static void log(String message) {
        info(message);
    }

    /**
     * Log a debug message - messages that can be hidden on production servers.
     * @param message
     */
    public static void debug(String message) {
        System.out.println("[SAKI|Rensa] " + getTimestamp() + " DEBUG: " + message);
    }

    /**
     * Alias for debug(String).
     * @param message
     */
    public static void db(String message) {
        debug(message);
    }

    /**
     * Alias for debug(String) using any Object's string representation.
     * @param message
     */
    public static void db(Object obj) {
        debug(obj.toString());
    }

}
