package com.edasaki.rensa.abstracts;

import java.util.ArrayList;

import com.edasaki.rensa.Rensa;
import com.edasaki.rensa.exceptions.NoRensaException;
import com.edasaki.rensa.logging.Saki;

/**
 * Base class for managers.
 * @author Edasaki
 */

public abstract class AbstractManager {

    private static ArrayList<AbstractManager> managers = new ArrayList<AbstractManager>();

    public static void registerAll() {
        if (Rensa.getInstance() == null)
            try {
                throw new NoRensaException();
            } catch (NoRensaException e) {
                e.printStackTrace();
            }
        for (AbstractManager am : managers) {
            Rensa.getInstance().registerListener(am);
        }
    }

    public static void create(Class<? extends AbstractManager> clazz) {
        try {
            managers.add(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public abstract void initialize();

    protected AbstractManager() {
        try {
            initialize();
            Saki.log("Loaded " + this.getClass().getSimpleName() + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
