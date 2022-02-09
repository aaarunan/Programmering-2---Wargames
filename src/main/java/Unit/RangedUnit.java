package Unit;

public class RangedUnit extends Unit {

    /**
     * An RangedUnit is a unit that has a ranged advantage.
     * It has a special ability where it receives less damage with increased range.
     * <p>
     * Stats:
     * attackPoints: 15
     * armorPoints: 8
     * attackBonus: 3
     * resistBonus: 6 4 2
     */

    private final int attackBonus = 3;

    private final int baseResistBonus = 2;
    private int resistBonus = 3 * baseResistBonus;

    /**
     * Constructs the CavalryUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */

    public RangedUnit(String name, int health) {
        super(name, health, 15, 8);
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
     * When the RangedUnit is hit it will call the super method,
     * and lower its resistBonus by the baseResistBonus until it is equal
     * to the baseResistBonus
     *
     * @param newHealthPoints the newHealthPoints of the unit
     */

    @Override
    public void hit(int newHealthPoints) {
        super.hit(newHealthPoints);

        if (resistBonus > baseResistBonus) {
            resistBonus -= baseResistBonus;
        }
    }
}
