package Unit;

import java.util.Comparator;
import java.util.Objects;

public abstract class Unit implements Comparable<Unit> {

    /**
     * A Unit is a superclass for a specific Unit.
     * A Unit needs to implement the methods:
     * getAttackBonus() and getResistBonus(), that specializes the unit.
     * <p>
     * The healthPoints of a Unit are points that determines the Unit state:
     * HealthPoints > 0 = alive,  healthPoints < 0 = dead
     * <p>
     * AttackPoints represents the base-attack of a Unit.
     * <p>
     * ArmorPoints is a layer of protection over the healthpoints.
     */

    private final String name;
    private int healthPoints;
    private int attackPoints;
    private int armorPoints;

    /**
     * Constructs the Unit with a given name, healthPoints and attackPoints
     *
     * @param name         must not be empty
     * @param healthPoints must be greater than 0
     * @param attackPoints must be greater than or equal to 0
     * @param armorPoints  must be greater than or equal to 0
     * @throws IllegalStateException if the following criteria is not met
     */

    public Unit(String name, int healthPoints, int attackPoints, int armorPoints) throws IllegalStateException {
        if (healthPoints <= 0 || attackPoints < 0 || armorPoints < 0) {
            throw new IllegalArgumentException("All fields must be greater than or equal to 0 (HP must be greater)");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must be given");
        }

        this.name = name;
        this.healthPoints = healthPoints;
        this.attackPoints = attackPoints;
        this.armorPoints = armorPoints;
    }

    /**
     * An abstract method for making a copy of the Unit.
     *
     * @return copy
     */

    public abstract Unit copy();

    /**
     * Attacking deals damage to a given opponent.
     * The damage is measured by:
     * opponent.healthPoints - this.attackPoints - this.attackBonus + opponent.getArmorPoints + opponent.resistBonus
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
     * Checks if the unit is dead. A Unit is dead if it has less than or equal to 0 healthPoints
     *
     * @return true if the unit is dead or false if it is alive
     */

    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * Get the name of the unit
     *
     * @return this.name
     */

    public String getName() {
        return name;
    }

    /**
     * Get the healthPoints of the Unit
     *
     * @return this.healthPoints
     */

    public int getHealthPoints() {
        return healthPoints;
    }

    /**
     * Get the attackPoints of
     *
     * @return this.attackPoints
     */
    public int getAttackPoints() {
        return attackPoints;
    }

    /**
     * get the armorPoints of the unit
     *
     * @return this.armorPoints
     */

    public int getArmorPoints() {
        return armorPoints;
    }

    /**
     * set the healthPoints of a Unit. this is usually done with combination with an attack.
     * The method is always called when it has been attacked.
     *
     * @param newHealthPoints the newHealthPoints of the Unit
     */

    public void hit(int newHealthPoints) {
        this.healthPoints = newHealthPoints;
    }

    /**
     * An abstract method that is used to specialize a specific Unit's attack
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

    @Override
    public String toString() {
        return
                "name: " + name +
                        " healthPoints: " + healthPoints +
                        " attackPoints: " + attackPoints +
                        " armorPoints: " + armorPoints;
    }

    @Override
    public int compareTo(Unit unit) {

        if(this == unit) {
            return 0;
        }

        int result = this.getName().compareTo(unit.getName());

        if (result == 0) {
            result = this.getHealthPoints() - unit.getHealthPoints();
        }

        return result;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return healthPoints == unit.healthPoints && attackPoints == unit.attackPoints && armorPoints == unit.armorPoints && name.equals(unit.name) && this.getAttackBonus() == unit.getAttackBonus() && this.getResistBonus() == unit.getResistBonus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, healthPoints, attackPoints, armorPoints, this.getAttackBonus(), this.getResistBonus());
    }
}
