package Unit;

public class CavalryUnit extends Unit {
    private int attackBonus = 6;
    private int resistBonus = 1;

    public CavalryUnit(String name, int health) {

        super(name, health, 20, 12);
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
