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

    protected final static int ATTACK_POINTS = 15;
    protected final static int ARMOR_POINTS = 8;

    protected final static int ATTACK_BONUS = 3;
    protected final static int BASE_RESIST_BONUS = 2;
    protected final static int RESIST_INTERVAL = 2;

    private int resistBonus = 3 * BASE_RESIST_BONUS;

    /**
     * Constructs the CavalryUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */

    public RangedUnit(String name, int health) {
        super(name, health, ATTACK_POINTS, ARMOR_POINTS);
    }


    /**
     * Constructor designed for Unit classes that needs to
     * change the stats for the Unit.
     *
     * @param name must not be empty
     * @param health must be greater than 0
     * @param attackPoints must be greater than 0
     * @param armorPoints must be greater than 0;
     */

    protected RangedUnit(String name, int health, int attackPoints, int armorPoints) {
        super(name, health, attackPoints, armorPoints);
    }

    @Override
    public RangedUnit copy() {
        return new RangedUnit(this.getName(), this.getHealthPoints());
    }

    @Override
    public int getAttackBonus() {
        return ATTACK_BONUS;
    }

    @Override
    public int getResistBonus() {
        return resistBonus;
    }

    /**
     * When the RangedUnit is hit it will call the super method,
     * and lower its resistBonus by the BASE_RESIST_BONUS until it is equal
     * to the BASE_RESIST_BONUS
     *
     * @param newHealthPoints the newHealthPoints of the unit
     */

    @Override
    public void hit(int newHealthPoints) {
        super.hit(newHealthPoints);

        if (resistBonus > BASE_RESIST_BONUS) {
            resistBonus -= RESIST_INTERVAL;
        }
    }
}
