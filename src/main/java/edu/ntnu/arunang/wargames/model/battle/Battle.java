package edu.ntnu.arunang.wargames.model.battle;

import edu.ntnu.arunang.wargames.model.Army;
import edu.ntnu.arunang.wargames.event.EventType;
import edu.ntnu.arunang.wargames.event.Subject;
import edu.ntnu.arunang.wargames.model.unit.Unit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * A Battle is a battlefield where two armies can fight. It stores an attacking Army and a defending Army.
 * <p>
 * The battle may also hold a terrain for simulation on terrain.
 * <p>
 * The class extends subject for notifying observers when simulating.
 */

public class Battle extends Subject {
    private boolean exit = false;

    private Army attacker;
    private Army defender;

    private Terrain terrain;

    private int numOfAttacks = 0;

    /**
     * Constructs a Battle with a defending army, and an attacking Army.
     *
     * @param attacker attacking Army.
     * @param defender defending Army.
     * @param terrain terrain of the battle
     */

    public Battle(Army attacker, Army defender, Terrain terrain) {
        this.attacker = attacker;
        this.defender = defender;
        this.terrain = terrain;
    }

    /**
     * Copy constructor used to create an identical new Battle.
     *
     * @return newly created battle.
     */

    public Battle copy() {
        return new Battle(this.attacker.copy(), this.defender.copy(), this.terrain);
    }

    /**
     * This simulates a fight. A random Unit from each army will attack a random Unit of the opposing Army. This happens
     * in a loop unit there is an army that has no units left to attack with.
     *
     * @return winning Army.
     * @throws IllegalStateException if the armies has no Units.
     */

    public Army simulate() throws IllegalStateException {

        prepareBattle(false);

        while (attacker.hasUnits() && defender.hasUnits()) {
            attack();
        }

        return getWinner();
    }

    /**
     * This simulates a fight. A random Unit from each army will attack a random Unit of the opposing Army. This happens
     * in a loop unit there is an army that has no units left to attack with. The simulation happens on a terrain.
     * <p>
     * When an attack has been done, the thread will be slept by 'delay' milliseconds. That can be used for slower
     * simulations in gui.
     * <p>
     * The simulation happens on a terrain. If the terrain is not set, the simulation will not start.
     *
     * @param delay the delay on each attack
     * @return the thread the simulation is running
     * @throws IllegalStateException    if the armies has no Units.
     * @throws IllegalArgumentException if the terrain is not set
     */

    public Army simulateDelayWithTerrain(int delay) throws IllegalStateException, IllegalArgumentException {
        // set exit to false
        prepareBattle(true);
        exit = false;

        while (attacker.hasUnits() && defender.hasUnits() && !exit) {
            attack();

            // sleep the thread
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(delay));
        }

        // notify observers if the simulation finished without interruptions
        if (!exit) {
            notifyObservers(EventType.FINISH);
        }

        return getWinner();
    }

    /**
     * Stops the simulation.
     */

    public void stopSimulation() {
        exit = true;
    }

    /**
     * Helper method for attacking once. Random unit from attacker army attacks a random defender unit. The armies get
     * swapped. If the attack is not in a terrain, null can be passed.
     */

    private void attack() {
        Army temp;
        Unit attackerUnit = attacker.getRandom();
        Unit defenderUnit = defender.getRandom();

        // check if the simulation is in a terrain
        if (terrain == null) {
            attackerUnit.attack(defenderUnit);
        } else {
            attackerUnit.attack(defenderUnit, terrain);
        }


        // remove the unit if it is dead
        if (defenderUnit.isDead()) {
            defender.remove(defenderUnit);
        }

        // notify observers
        notifyObservers(EventType.UPDATE);

        // swap attacker and defender
        temp = attacker;
        attacker = defender;
        defender = temp;

        numOfAttacks++;
    }

    /**
     * Checks if the armies are ready for simulation
     *
     * @param terrain simulation on a terrain
     * @throws IllegalStateException    if the armies has no Units.
     * @throws IllegalArgumentException if the terrain is not set
     */

    public void prepareBattle(boolean terrain) throws IllegalStateException, IllegalArgumentException {
        // check armies and terrain
        if (!attacker.hasUnits() || !defender.hasUnits()) {
            throw new IllegalStateException("All armies must have at least one unit.");
        }
        if (terrain && this.terrain == null) {
            throw new IllegalArgumentException("Terrain is null.");
        }

    }

    /**
     * Get the number of attacks in the battle. If noe simulations have been run numOfAttacks will be 0
     *
     * @return number of attacks
     */

    public int getNumOfAttacks() {
        return numOfAttacks;
    }

    /**
     * Get the losing army. Army is null if the simulation is not finisher
     *
     * @return losing army
     */

    public Army getLoser() {
        if (attacker.hasUnits() && defender.hasUnits()) {
            return null;
        }
        return attacker.hasUnits() ? defender : attacker;
    }

    /**
     * Get the winning army. Army is null if the simulation is not finished.
     *
     * @return winning army
     */

    public Army getWinner() {
        if (attacker.hasUnits() && defender.hasUnits()) {
            return null;
        }
        return attacker.hasUnits() ? attacker : defender;
    }

    /**
     * Get the terrain of where the battle is happening.
     *
     * @return terrain used for simulation
     */

    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Set the terrain.
     *
     * @param terrain new terrain.
     */

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    @Override
    public String toString() {
        return "Battle" + " attacker: " + attacker + " defender: " + defender;
    }
}
