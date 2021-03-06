package edu.ntnu.arunang.wargames.model.unit;

import edu.ntnu.arunang.wargames.model.battle.Terrain;

import java.util.Objects;

/**
 * A Unit is an abstract base class for a specific Unit. A unit is a military troop that can attack and deal damage, and
 * be attacked and take damage It implements Comparable because the Unit implements compareTo.
 * <p>
 * The healthPoints of a Unit are points that determines the Unit state: HealthPoints less than 0 = alive.
 * dead
 * <p>
 * AttackPoints represents the base-attack of a Unit.
 * <p>
 * ArmorPoints is a layer of protection over the healthPoints.
 */

public abstract class Unit implements Comparable<Unit> {

    private final String name;
    private final int attackPoints;
    private final int armorPoints;
    private int healthPoints;

    /**
     * Constructs the Unit with a given name, healthPoints and attackPoints and armorPoints
     *
     * @param name         must not be empty
     * @param healthPoints must be greater than 0
     * @param attackPoints must be greater than or equal to 0
     * @param armorPoints  must be greater than or equal to 0
     * @throws IllegalStateException if the before mentioned criterias are not met
     */

    public Unit(String name, int healthPoints, int attackPoints, int armorPoints) throws IllegalStateException {
        if (healthPoints < 0) {
            throw new IllegalArgumentException("Health-points can not be less than 0");
        }

        if (attackPoints < 0 || armorPoints < 0) {
            throw new IllegalArgumentException("Attack-points and armor-points must be greater than 0");
        }

        this.name = name;
        this.healthPoints = healthPoints;
        this.attackPoints = attackPoints;
        this.armorPoints = armorPoints;
    }

    /**
     * Attacking deals damage to a given opponent.
     * The damage is measured by: opponent.healthPoints - this.attackPoints
     * - this.attackBonus + opponent.getArmorPoints + opponent.resistBonus.
     *
     * @param opponent The opposing unit that is being attacked
     * @throws IllegalStateException if damage dealt is positive
     */

    public void attack(Unit opponent) throws IllegalStateException {
        int newHealthPoints = opponent.getHealthPoints() - this.getAttackPoints() - this.getAttackBonus()
                + opponent.getArmorPoints() + opponent.getResistBonus();

        opponent.setHealthPoints(newHealthPoints);
    }

    /**
     * Attacking deals damage to a given opponent. The damage is measured by: opponent.healthPoints - this.attackPoints
     * - this.attackBonus + opponent.getArmorPoints + opponent.resistBonus.
     * <p>
     * The attack is specified on a specific terrain
     *
     * @param opponent The opposing unit that is being attacked
     * @throws IllegalStateException if damage dealt is positive
     * @param terrain terrain enum
     */

    public void attack(Unit opponent, Terrain terrain) throws IllegalStateException {
        int newHealthPoints = opponent.getHealthPoints() - this.getAttackPoints() - this.getAttackBonus(terrain)
                + opponent.getArmorPoints() + opponent.getResistBonus(terrain);

        opponent.setHealthPoints(newHealthPoints);
    }

    /**
     * Creates another instance of the Unit, by copying every field in the current object.
     *
     * @return copy
     */

    public abstract Unit copy();

    /**
     * Makes a copy of the unit and resets its stats.
     * Essentially making it a new unit. Used to generalize Units.
     *
     * @return the unit with reset stats
     */

    public abstract Unit getResetCopy();

    /**
     * Checks if the unit is dead. A Unit is dead if it has less than or equal to 0 healthPoints.
     *
     * @return true if the unit is dead or false if it is alive
     */

    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * Get the name of the unit.
     *
     * @return this.name
     */

    public String getName() {
        return name;
    }

    /**
     * Get the healthPoints of the Unit.
     *
     * @return this.healthPoints
     */

    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * When a Unit is hit this function will be called. A unit is hit is when it is being attack, and usually takes
     * damage. If the healthpoints are lower than 0, the new healthpoints will be set to 0.
     *
     * @param newHealthPoints the newHealthPoints of the Unit
     */

    public void setHealthPoints(int newHealthPoints) {
        this.healthPoints = Integer.max(0, newHealthPoints);
    }

    /**
     * Get the attackPoints of the Unit.
     *
     * @return this.attackPoints
     */

    public int getAttackPoints() {
        return attackPoints;
    }

    /**
     * get the armorPoints of the Unit.
     *
     * @return this.armorPoints
     */

    public int getArmorPoints() {
        return armorPoints;
    }

    /**
     * An abstract method that is used to specialize the attack of an Unit.
     *
     * @return this.attackBonus
     */

    public abstract int getAttackBonus();

    /**
     * An abstract method that is used to specialize the defence of a specific Unit
     *
     * @return this.resistBonus
     */

    public abstract int getResistBonus();

    /**
     * An abstract method that is used to specialize the attack of an Unit.
     *
     * @param terrain of were the attack is happening
     * @return this.attackBonus
     */

    public abstract int getAttackBonus(Terrain terrain);

    /**
     * An abstract method that is used to specialize the defence of a specific Unit
     *
     * @param terrain the terrain of where the attack is happening
     * @return this.resistBonus
     */

    public abstract int getResistBonus(Terrain terrain);

    @Override
    public String toString() {
        return "Name: " + name + " HP: " + healthPoints + " Attack: " + attackPoints + " Armor: " + armorPoints
                + " Bonus(Attack/Resist): " + this.getAttackBonus() + "/" + this.getResistBonus();
    }

    /**
     * Compares to units together. In this sequence: name->healthPoints->armorPoints->attackBonus->defenceBonus.
     *
     * @param other Unit that is being compared to
     * @return integer that represents the difference
     */

    @Override
    public int compareTo(Unit other) {

        if (this.equals(other)) {
            return 0;
        }

        int result = this.getName().compareTo(other.getName());

        if (result == 0) {
            result = Integer.compare(this.getHealthPoints(), other.getHealthPoints());
        }

        if (result == 0) {
            result = Integer.compare(this.getAttackPoints(), other.getAttackPoints());
        }

        if (result == 0) {
            result = Integer.compare(this.getAttackBonus(), other.getAttackBonus());
        }

        if (result == 0) {
            result = Integer.compare(this.getResistBonus(), other.getResistBonus());
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit unit)) return false;
        return healthPoints == unit.healthPoints && attackPoints == unit.attackPoints && armorPoints == unit.armorPoints && name.equals(unit.name) && this.getAttackBonus() == unit.getAttackBonus() && this.getResistBonus() == unit.getResistBonus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, healthPoints, attackPoints, armorPoints, this.getAttackBonus(),
                this.getResistBonus());
    }
}
