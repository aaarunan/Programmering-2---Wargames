public class RangedUnit extends Unit {
    private int attackBonus = 3;
    private int resistBonus = 6;

    public RangedUnit(String type, int health) {
        super(type, health, 15, 8);
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
