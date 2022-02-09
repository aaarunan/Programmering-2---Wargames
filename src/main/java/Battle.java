import Unit.Unit;

public class Battle {

    /**
     * A Battle is where Armies fight.
     * A fight is done by a random Unit in a random Army attacking
     * the opposing Army.
     */

    private Army attacker;
    private Army defender;

    /**
     * Constructs a Battle by a defending army, and an attacking Army.
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
     * of the opposing Army. This happens in a loop unit there an army
     * has no units left to attack with.
     *
     * @return winning Army.
     * @throws IllegalStateException if the armies has no Units.
     */

    public Army simulate() throws IllegalStateException{
        if (!attacker.hasUnits() || !defender.hasUnits()) {
            throw new IllegalStateException("All armies must have atleast one unit.");
        }

        while (attacker.hasUnits() || defender.hasUnits()) {
            Unit attackerUnit = attacker.getRandom();
            Unit defenderUnit = defender.getRandom();

            attacker.getRandom().attack(defenderUnit);

            if (defenderUnit.isDead()) {
                attacker.remove(attackerUnit);
            }

            defender.getRandom().attack(attackerUnit);

            if (attackerUnit.isDead()) {
                defender.remove(defenderUnit);
            }
        }

        return attacker.hasUnits() ? attacker : defender;

    }

    @Override
    public String toString() {
        return "Battle" +
                "attacker: " + attacker +
                "defender: " + defender;
    }
}
