package Unit;

public class CommanderUnit extends Unit {

    /**
     * The CommanderUnit is a more capable CavalryUnit.
     * It has the same special ability aswell.
     * <p>
     * Stats:
     * attackPoints: 25,
     * armorPoints: 15,
     * attackBonus: 4+2,
     * resistBonus: 1
     */

    private final int baseAttackBonus = 2;
    private int attackBonus = 4 + baseAttackBonus;

    private final int resistBonus = 1;

    /**
     * Constructs the CommanderUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */

    public CommanderUnit(String name, int health) {
        super(name, health, 25, 15);
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
