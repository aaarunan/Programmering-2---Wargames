package Unit;

import java.util.Objects;

public abstract class Unit {

    /**
     *A Unit is a superclass for a troop.
     */

    private final String name;
    private int healthPoints;
    private int attackPoints;
    private int armorPoints;

    public Unit(String name, int healthPoints, int attackPoints, int armorPoints) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.attackPoints = attackPoints;
        this.armorPoints = armorPoints;
    }

    public void attack(Unit opponent) {
        int newHealthPoints = opponent.getHealthPoints() - this.getAttackPoints() - this.getAttackBonus() + opponent.getArmorPoints() + opponent.getResistBonus();
        opponent.setHealthPoints(newHealthPoints);
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public void setHealthPoints(int newHealthPoints) {
        this.healthPoints = newHealthPoints;
    }

    public abstract int getAttackBonus();

    public abstract int getResistBonus();

    @Override
    public String toString() {
        return
                "Type" + name +
                " healthPoints=" + healthPoints +
                " attackPoints=" + attackPoints +
                " armorPoints=" + armorPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return healthPoints == unit.healthPoints && attackPoints == unit.attackPoints && armorPoints == unit.armorPoints && name.equals(unit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, healthPoints, attackPoints, armorPoints);
    }
}
