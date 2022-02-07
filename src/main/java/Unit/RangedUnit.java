package Unit;

public class RangedUnit extends Unit {
    private int attackBonus = 3;
    private int resistBonus = 6;

    public RangedUnit(String name, int health) {
        super(name, health, 15, 8);
    }

    @Override
    public int getAttackBonus() {
        return attackBonus;
    }

    @Override
    public int getResistBonus() {
        return resistBonus;
    }
}
