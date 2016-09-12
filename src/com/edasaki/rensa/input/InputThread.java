package com.edasaki.rensa.input;

import java.util.Scanner;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.logging.Saki;

/**
 * A thread that reads input from System.in (console) and
 * passes it to {@link InputManager} for parsing.
 * @author Edasaki
 */

public class InputThread extends Thread {
    private volatile boolean running = true;

    protected void terminate() {
        running = false;
    }

    public void run() {
        while (!Rensa.isReady()) {
            // Don't do anything until Rensa is fully loaded.
            try {
                // Don't spam isReady() checks.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Saki.db("input thread starting scanner...");
        // System.in should NOT be closed.
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        while (running) {
            if (scan.hasNextLine()) {
                String s = scan.nextLine();
                try {
                    InputManager.consoleCommand(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
