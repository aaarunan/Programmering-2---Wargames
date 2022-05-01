package edu.ntnu.arunang.wargames.gui;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.fsh.FSH;
import edu.ntnu.arunang.wargames.fsh.FileFormatException;
import edu.ntnu.arunang.wargames.gui.factory.AlertFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Singleton for storing settings and choosen attacker and defender.. Works as a cache, so there is no need
 * to load again. It can also sort the cached armies. It also caches an attacking -
 * and defending army for simulation purposes.
 */

public class ArmySingleton {
    private static List<Army> armies;
    private static Army attacker;
    private static Army defender;
    private static boolean simulate;
    private static ArmySingleton single_instance = null;

    /**
     * Private constructor for initializing the Singleton. Can not
     * be instantiated outside of class.
     */

    private ArmySingleton() {
        armies = new ArrayList<>();
    }

    /**
     * Initializes the object, only if singe_instance is not instantiated.
     *
     * @return the single instance
     */

    public static ArmySingleton getInstance() {
        if (single_instance == null)
            single_instance = new ArmySingleton();

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
        ArmySingleton.simulate = simulate;
    }

    /**
     * Get the cached armies.
     *
     * @return List of the cached armies
     */

    public List<Army> getArmies() {
        return armies;
    }

    /**
     * Set the cached armies. Usually done when the armies have been loaded from files.
     *
     * @param armies List of armies
     */

    public void setArmies(List<Army> armies) {
        if (armies == null) {
            return;
        }

        ArmySingleton.armies = new ArrayList<>();
        ArmySingleton.armies.addAll(armies);
    }

    /**
     * Get the armies sorted by count.
     *
     * @return List sorted by count
     */

    public List<Army> getArmiesSortedByCount() {
        return armies.stream().sorted(Comparator.comparingInt(Army::size)).toList();
    }

    /**
     * Get the armies sorted by name
     *
     * @return List sorted by name
     */

    public List<Army> getArmiesSortedByName() {
        return armies.stream().sorted(Comparator.comparing(Army::getName)).toList();
    }

    /**
     * Add an army to the army-list.
     *
     * @param army that is added
     */

    public void addArmy(Army army) {
        ArmySingleton.armies.add(army);
    }

    /**
     * Removes an army from the singleton.
     *
     * @param army army that is being removed.
     */

    public void removeArmy(Army army) {
        ArmySingleton.armies.remove(army);
    }

    /**
     * Get the attacking army.
     *
     * @return attacker army
     */

    public Army getAttacker() {
        return attacker;
    }

    /**
     * Set the attacking army.
     *
     * @param attacker attacking army
     */

    public void setAttacker(Army attacker) {
        ArmySingleton.attacker = attacker;
    }

    /**
     * Get the defending army.
     *
     * @return defending army
     */

    public Army getDefender() {
        return defender;
    }

    /**
     * Set the defending unit.
     *
     * @param defender defending unit
     */


    public void setDefender(Army defender) {
        ArmySingleton.defender = defender;
    }

    /**
     * Clear the singleton. This does NOT clear the cached armies. Used when
     * finishing simulations.
     */

    public void clear() {
        attacker = null;
        defender = null;
        simulate = false;
    }
}
