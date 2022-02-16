package Unit;

public class CommanderUnit extends CavalryUnit {

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

    private final static int ATTACK_POINTS = 25;
    private final static int ARMOR_POINTS = 15;

    /**
     * Constructs the CommanderUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */


    public CommanderUnit(String name, int health) {
        super(name, health, ATTACK_POINTS, ARMOR_POINTS);
    }


    @Override
    public CommanderUnit copy() {
        return new CommanderUnit(this.getName(), this.getHealthPoints());
    }

}
