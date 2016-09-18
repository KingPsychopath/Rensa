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

    private static ArrayList<Class<? extends AbstractManager>> queuedManagers = new ArrayList<Class<? extends AbstractManager>>();

    public static final void registerAll() {
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

    public static final void processQueuedManagers() {
        Saki.log("Setting up managers...");
        for (Class<? extends AbstractManager> c : queuedManagers)
            create(c, true);
    }

    public static final void create(Class<? extends AbstractManager> clazz) {
        create(clazz, false);
    }

    public static final void create(Class<? extends AbstractManager> clazz, boolean preload) {
        if (preload || Rensa.isReady()) {
            try {
                AbstractManager am = clazz.newInstance();
                managers.add(am);
                am.initialize();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            queuedManagers.add(clazz);
        }
    }

    public abstract void initialize();

    protected AbstractManager() {
        try {
            Saki.log("Loaded " + this.getClass().getSimpleName() + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
