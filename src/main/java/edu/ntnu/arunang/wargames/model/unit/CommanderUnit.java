package edu.ntnu.arunang.wargames.model.unit;

/**
 * The CommanderUnit is a more capable CavalryUnit. It has the same special ability aswell.
 * <p>
 * Stats: attackPoints: 25, armorPoints: 15, attackBonus: 4+2, resistBonus: 1
 */

public class CommanderUnit extends CavalryUnit {

    protected final static int ATTACK_POINTS = 25;
    protected final static int ARMOR_POINTS = 15;

    /**
     * Constructs the CommanderUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */

    public CommanderUnit(String name, int health) {
        super(name, health, ATTACK_POINTS, ARMOR_POINTS);
    }

    /**
     * Constructor designed for objects in the same package that needs access to change the stats of the Unit.
     *
     * @param name         must not be empty
     * @param health       must be greater than 0
     * @param attackPoints must be greater than 0
     * @param armorPoints  must be greater than 0;
     */

    protected CommanderUnit(String name, int health, int attackPoints, int armorPoints) {
        super(name, health, attackPoints, armorPoints);
    }

    @Override
    public CommanderUnit copy() {
        CommanderUnit copy = getResetCopy();
        copy.hasAttacked = hasAttacked;
        return copy;
    }

    @Override
    public CommanderUnit getResetCopy() {
        return new CommanderUnit(getName(), getHealthPoints(), getAttackPoints(), getArmorPoints());
    }

}
