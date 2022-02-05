import Unit.Unit;

public class Battle {
    private Army attacker;
    private Army defender;

    public Battle(Army attacker, Army defender) {
        this.attacker = attacker;
        this.defender = defender;

    }

    public Army simulate() {
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
