package Unit;

public class InfantryUnit extends Unit {

    /**
     * An InfantryUnit is a specialized melee unit that does not have a great defense.
     * <p>
     * Stats:
     * attackPoints of 15,
     * armorPoints of 10,
     * attackBonus of 2,
     * resistBonus of 1
     */

    private final int attackBonus = 2;

    private final int resistBonus = 1;

    /**
     * Constructs the InfantryUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */

    public InfantryUnit(String name, int health) {
        super(name, health, 15, 10);
    }

    @Override
    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public int getResistBonus() {
        return resistBonus;
    }


}
