

public abstract class Unit {

    /**
     *A Unit is a superclass for a troop.
     */

    private final String type;
    private int healthPoints;
    private int attackPoints;
    private int armorPoints;

    public Unit(String type, int healthPoints, int attackPoints, int armorPoints) {
        this.type = type;
        this.healthPoints = healthPoints;
        this.attackPoints = attackPoints;
        this.armorPoints = armorPoints;
    }

    public void attack(Unit opponent) {
        int newHealthPoints = opponent.getHealthPoints()-attackPoints+this.getAttackBonus() + opponent.attackPoints +opponent.getResistBonus();
        opponent.setHealthPoints(newHealthPoints);
    }

    public String getType() {
        return type;
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

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public abstract int getAttackBonus();

    public abstract int getResistBonus();

    @Override
    public String toString() {
        return
                "Type" + type +
                " healthPoints=" + healthPoints +
                " attackPoints=" + attackPoints +
                " armorPoints=" + armorPoints;
    }
}
