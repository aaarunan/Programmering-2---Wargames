package edu.ntnu.arunang.wargames.model.unit;

import edu.ntnu.arunang.wargames.model.battle.Terrain;

/**
 * An InfantryUnit is a specialized melee unit that does not have a great defense.
 * <p>
 * Stats: attackPoints of 15, armorPoints of 10, attackBonus of 2, resistBonus of 1
 * <p>
 * Terrain: +2 attack - and resistbonus on FOREST
 */

public class InfantryUnit extends Unit {

    protected static final int ATTACK_POINTS = 15;
    protected static final int ARMOR_POINTS = 10;

    protected static final int ATTACK_BONUS = 2;
    protected static final int RESIST_BONUS = 1;
    protected static final int FOREST_BONUS = 2;

    /**
     * Constructs the InfantryUnit with the given stats
     *
     * @param name   must not be empty
     * @param health must be greater than 0
     */

    public InfantryUnit(String name, int health) {
        super(name, health, ATTACK_POINTS, ARMOR_POINTS);
    }

    /**
     * Constructor designed for Unit classes that needs to change the stats for the Unit.
     *
     * @param name         must not be empty
     * @param health       must be greater than 0
     * @param attackPoints must be greater than 0
     * @param armorPoints  must be greater than 0;
     */

    protected InfantryUnit(String name, int health, int attackPoints, int armorPoints) {
        super(name, health, attackPoints, armorPoints);
    }

    @Override
    public InfantryUnit copy() {
        return new InfantryUnit(getName(), getHealthPoints(), getAttackPoints(), getArmorPoints());
    }

    @Override
    public InfantryUnit getResetCopy() {
        return copy();
    }

    @Override
    public int getAttackBonus() {
        return ATTACK_BONUS;
    }

    @Override
    public int getResistBonus() {
        return RESIST_BONUS;
    }

    @Override
    public int getAttackBonus(Terrain terrain) {
        return terrain.equals(Terrain.FOREST) ? getAttackBonus() : getAttackBonus()+ FOREST_BONUS;
    }

    @Override
    public int getResistBonus(Terrain terrain) {
        return terrain.equals(Terrain.FOREST) ? getResistBonus() : getResistBonus()+ FOREST_BONUS;
    }
}
