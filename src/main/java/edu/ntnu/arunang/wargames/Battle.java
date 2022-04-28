package edu.ntnu.arunang.wargames;

import edu.ntnu.arunang.wargames.gui.controller.SimulateCON;
import edu.ntnu.arunang.wargames.observer.HitObserver;
import edu.ntnu.arunang.wargames.observer.Subject;
import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.application.Platform;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A Battle is a battlefield where two armies can fight.
 * A battle has an attacking Army and a defending Army.
 */

public class Battle extends Subject {

    private Army attacker;
    private Army defender;

    private Army winner;
    private Army loser;

    private int numOfAttacks = 0;

    /**
     * Constructs a Battle with a defending army, and an attacking Army.
     *
     * @param attacker attacking Army.
     * @param defender defending Army.
     */

    public Battle(Army attacker, Army defender) {
        this.attacker = attacker;
        this.defender = defender;

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
            attack();
        }

        return getConclusion();
    }

    /**
     * This simulates a fight.
     * A random Unit from each army will attack a random Unit
     * of the opposing Army. This happens in a loop unit there is an army
     * that has no units left to attack with. The simulation happens on a terrain
     * Each attack is backed up by a delay that sleeps the thread.
     *
     * @param delay the delay on each attack
     * @return winning Army.
     * @throws IllegalStateException if the armies has no Units.
     */

    public Army simulate(int delay, Terrain terrain, SimulateCON simulateCON) {
        if (!attacker.hasUnits() || !defender.hasUnits()) {
            throw new IllegalStateException("All armies must have atleast one unit.");
        }
        int updateRate = 25;
        new Thread(() -> {
            AtomicInteger i = new AtomicInteger(updateRate);
            AtomicInteger j = new AtomicInteger(updateRate/3);


            while (attacker.hasUnits() && defender.hasUnits()) {
                attack(terrain);

                if (i.decrementAndGet() <= 0) {
                    Platform.runLater(() -> simulateCON.updateBarChart(numOfAttacks));
                    i.set(updateRate);
                }

                if (j.decrementAndGet() <= 0) {
                    Platform.runLater(simulateCON::updateArmies);
                    j.set(updateRate/2);
                }

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(()-> {
                simulateCON.updateArmies(winner, loser);
                simulateCON.updateBarChart(numOfAttacks);
            });
        }).start();
        return getConclusion();
    }

    private void attack() {
        notifyObservers();

        Army temp;
        Unit attackerUnit = attacker.getRandom();
        Unit defenderUnit = defender.getRandom();

        attackerUnit.attack(defenderUnit);

        if (defenderUnit.isDead()) {
            defender.remove(defenderUnit);
        }

        temp = attacker;
        attacker = defender;
        defender = temp;

        numOfAttacks++;
    }

    /**
     * Attack once. Random unit from attacker army
     * attacks a random defender unit. The armies get swapped.
     *
     * @param terrain the terrain the battle is happening in
     */
    private void attack(Terrain terrain) {
        notifyObservers();

        Army temp;
        Unit attackerUnit = attacker.getRandom();
        Unit defenderUnit = defender.getRandom();

        attackerUnit.attack(defenderUnit, terrain);

        if (defenderUnit.isDead()) {
            defender.remove(defenderUnit);
        }

        temp = attacker;
        attacker = defender;
        defender = temp;

        numOfAttacks++;
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
        return loser;
    }


    /**
     * Get the winning army. Army is null if the simulation has not been run.
     *
     * @return winning army
     */

    public Army getWinner() {
        return winner;
    }

    /**
     * Get which army won. Checks which army has no units.
     *
     * @return winning amry
     */

    private Army getConclusion() {
        if (attacker.hasUnits()) {
            winner = attacker;
            loser = defender;
        } else {
            winner = defender;
            loser = attacker;
        }

        return winner;
    }

    @Override
    public String toString() {
        return "Battle" +
                " attacker: " + attacker +
                " defender: " + defender;
    }
}
