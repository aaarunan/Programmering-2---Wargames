package edu.ntnu.arunang.wargames.battle;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Terrain;
import edu.ntnu.arunang.wargames.event.EventType;
import edu.ntnu.arunang.wargames.event.Subject;
import edu.ntnu.arunang.wargames.unit.Unit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * A Battle is a battlefield where two armies can fight.
 * It stores an attacking Army and a defending Army.
 */

public class Battle extends Subject {

    private boolean exit = false;
    private int delay;

    private Army attacker;
    private Army defender;

    private Terrain terrain;

    private int numOfAttacks = 0;

    /**
     * Constructs a Battle with a defending army, and an attacking Army.
     *
     * @param attacker attacking Army.
     * @param defender defending Army.
     */

    public Battle(Army attacker, Army defender, Terrain terrain) {
        this.attacker = attacker;
        this.defender = defender;
        this.terrain = terrain;
    }

    /**
     * This simulates a fight.
     * A random Unit from each army will attack a random Unit
     * of the opposing Army. This happens in a loop unit there is an army
     * that has no units left to attack with.
     *
     * @return winning Army.
     * @throws IllegalStateException if the armies has no Units.
     */

    public Army simulate() throws IllegalStateException {
        if (!attacker.hasUnits() || !defender.hasUnits()) {
            throw new IllegalStateException("All armies must have atleast one unit.");
        }
        while (attacker.hasUnits() && defender.hasUnits()) {
            attack(null);
        }

        return attacker.hasUnits() ? attacker : defender;
    }

    /**
     * This simulates a fight.
     * A random Unit from each army will attack a random Unit
     * of the opposing Army. This happens in a loop unit there is an army
     * that has no units left to attack with. The simulation happens on a terrain.
     * <p>
     * Each attack is backed up by a delay that sleeps the thread.The simulation happens
     *
     * @param delay the delay on each attack
     * @return the thread the simulation is running
     * @throws IllegalStateException if the armies has no Units.
     */

    public Army simulateDelayWithTerrain(int delayMilliseconds) {
        if (!attacker.hasUnits() || !defender.hasUnits()) {
            throw new IllegalStateException("All armies must have atleast one unit.");
        }
        if (terrain == null) {
            throw new IllegalArgumentException("Terrain is null.");
        }

        this.delay = delayMilliseconds;
        exit = false;

        while (attacker.hasUnits() && defender.hasUnits() && !exit) {
            attack(terrain);
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(delayMilliseconds));
        }

        if (!exit) {
            notifyObservers(EventType.FINISH);
        }
        return attacker.hasUnits() ? attacker : defender;
    }

    /**
     * Stops all currently running simulations.
     */

    public void stopSimulation() {
        exit = true;
    }

    /**
     * Attack once. Random unit from attacker army
     * attacks a random defender unit. The armies get swapped.
     *
     * @param terrain the terrain the battle is happening in
     */

    private void attack(Terrain terrain) {
        Army temp;
        Unit attackerUnit = attacker.getRandom();
        Unit defenderUnit = defender.getRandom();

        if (terrain == null) {
            attackerUnit.attack(defenderUnit);
        } else {
            attackerUnit.attack(defenderUnit, terrain);
        }
        notifyObservers(EventType.UPDATE);

        if (defenderUnit.isDead()) {
            defender.remove(defenderUnit);
        }

        temp = attacker;
        attacker = defender;
        defender = temp;

        numOfAttacks++;
    }

    public Battle copy() {
        return new Battle(this.attacker.copy(), this.defender.copy(), this.terrain);
    }

    /**
     * Get the number of attacks in the battle.
     * If noe simulations have been run numOfAttacks will be 0
     *
     * @return number of attacks
     */

    public int getNumOfAttacks() {
        return numOfAttacks;
    }

    /**
     * Get the losing army. Army is null if the simulation has not been run.
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
     * Get the winning army. Army is null if the simulation has not been run.
     *
     * @return winning army
     */

    public Army getWinner() {
        if (attacker.hasUnits() && defender.hasUnits()) {
            return null;
        }
        return attacker.hasUnits() ? attacker : defender;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    /**
     * Get which army won. Checks which army has no units.
     *
     * @return winning army
     */

    @Override
    public String toString() {
        return "Battle" +
                " attacker: " + attacker +
                " defender: " + defender;
    }
}
