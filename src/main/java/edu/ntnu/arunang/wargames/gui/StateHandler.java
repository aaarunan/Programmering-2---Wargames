package edu.ntnu.arunang.wargames.gui;

/**
 *  This singleton handles session specific details. Works as a cache. Currently, it only saves
 *  whether the user wants to simulate. It can store further session settings aswell.
 */

public class StateHandler {
    private static boolean simulate;
    private static StateHandler single_instance = null;

    /**
     * Private constructor for initializing the Singleton. Can not
     * be instantiated outside of class.
     */

    private StateHandler() {
    }

    /**
     * Initializes the object, only if singe_instance is not instantiated.
     *
     * @return the single instance
     */

    public static StateHandler getInstance() {
        if (single_instance == null)
            single_instance = new StateHandler();

        return single_instance;
    }

    /**
     * Set the simulation variable
     *
     * @return boolean
     */

    public boolean isSimulate() {
        return simulate;
    }

    /**
     * Set the simulation variable
     *
     * @param simulate true or false
     */

    public void setSimulate(boolean simulate) {
        StateHandler.simulate = simulate;
    }
}
