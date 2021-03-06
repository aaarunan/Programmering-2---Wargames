package edu.ntnu.arunang.wargames.model.unit;

import edu.ntnu.arunang.wargames.model.battle.Terrain;

/**
 * A RangedUnit is a Unit that has a ranged speciality. It has a special ability where it receives less damage with
 * increased range. The resistBonus will go down in increments of 2.
 * <p>
 * Stats: attackPoints: 15 armorPoints: 8 attackBonus: 3 resistBonus: 6 4 2
 * <p>
 * Terrain: +2 attackbonus on HILL -2 attackbonus on FOREST
 */

public class RangedUnit extends Unit {

    protected final static int ATTACK_POINTS = 15;
    protected final static int ARMOR_POINTS = 8;

    protected final static int ATTACK_BONUS = 3;
    protected final static int BASE_RESIST_BONUS = 2;
    protected static final int HILL_ATTACK_BONUS = 2;
    protected static final int FOREST_ATTACK_BONUS = -2;
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
     * Constructor for objects in the same package that needs access to all fields.
     *
     * @param name         must not be empty
     * @param health       must be greater than 0
     * @param attackPoints must be greater than 0
     * @param armorPoints  must be greater than 0;
     */

    protected RangedUnit(String name, int health, int attackPoints, int armorPoints) {
        super(name, health, attackPoints, armorPoints);
    }

    @Override
    public RangedUnit copy() {
        RangedUnit copy = getResetCopy();
        copy.resistBonus = resistBonus;
        return copy;
    }

    @Override
    public RangedUnit getResetCopy() {
        return new RangedUnit(getName(), getHealthPoints(), getAttackPoints(), getArmorPoints());
    }

    @Override
    public int getAttackBonus() {
        return ATTACK_BONUS;
    }

    @Override
    public int getResistBonus() {
        return resistBonus;
    }

    @Override
    public int getAttackBonus(Terrain terrain) {
        int bonus = getAttackBonus();
        if (terrain.equals(Terrain.HILL)) {
            bonus += HILL_ATTACK_BONUS;
        } else if (terrain.equals(Terrain.FOREST)) {
            bonus += FOREST_ATTACK_BONUS;
        }
        return bonus;
    }

    @Override
    public int getResistBonus(Terrain terrain) {
        return getResistBonus();
    }

    /**
     * When the RangedUnit is hit the resistBonus will go down in BASE_RESIST_INCREMENT till it is equal to
     * BASE_RESIST_BONUS
     *
     * @param newHealthPoints the newHealthPoints of the Unit
     */

    @Override
    public void setHealthPoints(int newHealthPoints) {
        super.setHealthPoints(newHealthPoints);

        Integer.max(BASE_RESIST_BONUS, resistBonus -= RESIST_INTERVAL);
    }
}
