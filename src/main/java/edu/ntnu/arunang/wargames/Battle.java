package edu.ntnu.arunang.wargames;

import edu.ntnu.arunang.wargames.observer.Subject;
import edu.ntnu.arunang.wargames.unit.Unit;

/**
 * A Battle is a battlefield where two armies can fight.
 * A battle has an attacking Army and a defending Army.
 */

public class Battle extends Subject {

    private Army attacker;
    private Army defender;

    private Army winner = new Army("Winner");
    private Army loser = new Army("Loser");

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

    public Army simulate(int delay) {
        if (!attacker.hasUnits() || !defender.hasUnits()) {
            throw new IllegalStateException("All armies must have atleast one unit.");
        }
        System.out.println("Thread simulate: " + Thread.currentThread());
        while (attacker.hasUnits() && defender.hasUnits()) {

            attack();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public int getNumOfAttacks() {
        return numOfAttacks;
    }

    public Army getLoser() {
        return loser;
    }

    public Army getWinner() {
        return winner;
    }

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
