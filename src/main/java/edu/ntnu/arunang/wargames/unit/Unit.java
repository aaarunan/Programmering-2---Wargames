package edu.ntnu.arunang.wargames.unit;

import java.util.Objects;

/**
 * A Unit is an abstract base class for a specific Unit.
 * A unit is a military troop that can attack and deal damage, and be attacked and take damage
 * It implements Comparable because the Unit implements compareTo.
 * <p>
 * The healthPoints of a Unit are points that determines the Unit state:
 * HealthPoints > 0 = alive,  healthPoints < 0 = dead
 * <p>
 * AttackPoints represents the base-attack of a Unit.
 * <p>
 * ArmorPoints is a layer of protection over the healthPoints.
 */

public abstract class Unit implements Comparable<Unit> {

    private final String name;
    private int healthPoints;
    private final int attackPoints;
    private final int armorPoints;

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
        if (healthPoints <= 0) {
            throw new IllegalArgumentException("Health-points must be greater than or equal to 0");
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
     * Creates another instance of the Unit, by copying every field in the current object.
     *
     * @return copy
     */

    public abstract Unit copy();

    /**
     * Makes a copy of the unit and resets its stats. Essentially making it a new unit.
     * Used to generalize Units.
     */

    public abstract Unit getResetCopy();

    /**
     * Attacking deals damage to a given opponent.
     * The damage is measured by:
     * opponent.healthPoints - this.attackPoints - this.attackBonus + opponent.getArmorPoints + opponent.resistBonus.
     *
     * @param opponent The opposing unit that is being attacked
     * @throws IllegalStateException if damage dealt is positive
     */

    public void attack(Unit opponent) throws IllegalStateException {
        int newHealthPoints = opponent.getHealthPoints() -
                this.getAttackPoints() -
                this.getAttackBonus() +
                opponent.getArmorPoints() +
                opponent.getResistBonus();

        if (newHealthPoints > opponent.getHealthPoints()) {
            throw new IllegalStateException("Attacking can not give healthPoints");
        }
        opponent.hit(newHealthPoints);
    }

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
     * When a Unit is hit this function will be called.
     * A unit is hit is when it is being attack, and usually takes damage.
     *
     * @param newHealthPoints the newHealthPoints of the Unit
     */

    public void hit(int newHealthPoints) {
        this.healthPoints = newHealthPoints;
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
     * Converts the unit to csv. Used to save the unit to a file.
     *
     * @return string containing the unit
     */

    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(getClass().getSimpleName()).append(",").append(this.getName()).append(",").append(this.getHealthPoints());

        return sb.toString();
    }

    @Override
    public String toString() {
        return
                "Name: " + name +
                        " HP: " + healthPoints +
                        " Attack: " + attackPoints +
                        " Armor: " + armorPoints +
                        " Bonus(Attack/Resist): " + this.getAttackBonus() +
                        "/" + this.getResistBonus();
    }

    /**
     * Compares to units together. In this sequence:
     * name->healthPoints->armorPoints->attackBonus->defenceBonus.
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
        return Objects.hash(name, healthPoints, attackPoints, armorPoints, this.getAttackBonus(), this.getResistBonus());
    }
}
