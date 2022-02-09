package Unit;

public class CavalryUnit extends Unit {

    /**
     * A CavalryUnit is a specialized melee unit, that does not have great defense.
     * CavalryUnit has a special ability where its first hit deals more damage.
     * <p>
     * Stats:
     * attackPoints of 20,
     * armorPoints of 12,
     * attackBonus of 4+2,
     * resistBonus of 1
     */

    private final int baseAttackBonus = 2;
    private int attackBonus = 4 + baseAttackBonus;

    private final int resistBonus = 1;

    /**
     * Constructs the CavalryUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */

    public CavalryUnit(String name, int health) {
        super(name, health, 20, 12);
    }

    @Override
    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public int getResistBonus() {
        return resistBonus;
    }

    /**
     * Calls the super method, and manages the units special ability.
     * Attacking once will demote to baseAttackBonus
     *
     * @param opponent The opposing unit that is being attacked
     */

    @Override
    public void attack(Unit opponent) {
        super.attack(opponent);

        if (attackBonus != baseAttackBonus) {
            attackBonus = baseAttackBonus;
        }
    }
}
