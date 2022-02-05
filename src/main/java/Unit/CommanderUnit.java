package Unit;

public class CommanderUnit extends Unit {
    private int attackBonus = 6;
    private int resistBonus = 1;

    public CommanderUnit(String name, int health) {
        super(name, health, 25, 15);
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
