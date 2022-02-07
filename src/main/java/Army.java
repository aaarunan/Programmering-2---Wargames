import Unit.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Army {
    private final String name;
    private final ArrayList<Unit> units;

    private final Random rand = new Random();

    public Army(String name) {
        this.name = name;
        this.units = new ArrayList<>();
    }

    public Army(String name, ArrayList<Unit> units) {
        this.name = name;
        this.units = units;
    }

    public Unit get(int index) {
        return units.get(index);
    }

    public void add(Unit unit) {
        units.add(unit);
    }

    public void addAll(ArrayList<Unit> units) {
        this.units.addAll(units);
    }

    public void remove(Unit unit) {
        units.removeIf(target -> target.equals(unit));
    }

    public boolean hasUnits() {
        return !units.isEmpty();
    }

    public Unit getRandom() {
        return units.get(rand.nextInt(units.size()));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Unit unit : this.units) {
            sb.append(unit.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Army)) return false;
        Army army = (Army) o;
        return name.equals(army.name) && Objects.equals(units, army.units);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, units);
    }
}
