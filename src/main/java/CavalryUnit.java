public class CavalryUnit extends Unit {
    private int attackBonus = 6;
    private int resistBonus = 1;

    public CavalryUnit(String type, int health) {
        super(type, health, 20, 12);
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
