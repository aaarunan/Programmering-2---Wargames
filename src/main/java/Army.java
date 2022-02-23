import Unit.*;

import java.util.*;

public class Army {

    /**
     * An Army is a collection of Units.
     * It has a name that defines it.
     * <p>
     * The Army class uses an ArrayList for storing the Units.
     * This is because there can be stored multiples of the same Unit,
     * and the order of the Units does not matter.
     */

    private final String name;
    private final ArrayList<Unit> units;

    private final Random rand = new Random(); //Used to get a random Unit

    /**
     * Constructs the Army with an empty ArrayList.
     *
     * @param name must not be empty.
     * @throws IllegalArgumentException if the name is blank.
     */

    public Army(String name) throws IllegalArgumentException {
        if (name.isBlank()) {
            throw new IllegalArgumentException(("Name must not be blank."));
        }

        this.name = name;
        this.units = new ArrayList<>();

    }

    /**
     * Constructs the Army with a given ArrayList of Units
     *
     * @param name  must not be empty.
     * @param units ArrayList of Units.
     * @throws IllegalArgumentException if the name is blank.
     */

    public Army(String name, ArrayList<Unit> units) throws IllegalArgumentException {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank.");
        }

        this.name = name;
        this.units = units;
    }

    /**
     * Get a specific Unit in the Army by a given index.
     *
     * @param index in the collection.
     * @return the targeted Unit.
     */

    public Unit get(int index) {
        return units.get(index);
    }

    /**
     * Add a Unit to the Army.
     *
     * @param unit Unit that is being added.
     */

    public void add(Unit unit) {
        units.add((unit.copy()));
    }

    /**
     * Adding multiples of the same Unit.
     *
     * @param unit Unit that is added
     * @param count how many there should be added.
     */
    public void add(Unit unit, int count) {
        for (int i = 0; i < count; i++) {
            units.add(unit.copy());
        }
    }

    /**
     * Add an ArrayList of Units to the Army.
     *
     * @param units that is being added.
     */

    public void add(ArrayList<Unit> units) {
        for (Unit unit : units) {
            this.add(unit.copy());
        }
    }

    /**
     * Remove a Unit in the Army.
     * Usually done if the Units is dead.
     *
     * @param unit the Unit that is being removed.
     * @return true if removed, or false if not removed.
     */

    public boolean remove(Unit unit) {
        return units.remove(unit);
    }

    /**
     * Checks if there are no Units in the Army.
     *
     * @return false if empty, true if there is atleast one Unit.
     */

    public boolean hasUnits() {
        return !units.isEmpty();
    }

    /**
     * Get a random Unit in the Army.
     *
     * @return A random Unit.
     * @throws IllegalStateException if the Army has no Units.
     */

    public Unit getRandom() throws IllegalStateException {
        if (!this.hasUnits()) {
            throw new IllegalStateException("Army has no units");
        }

        Unit random = units.get(rand.nextInt(units.size()));

        return random;
    }

    protected ArrayList<Unit> sort() {
        ArrayList<Unit> copy = new ArrayList<>(this.units);

        Collections.sort(copy);

        return copy;
    }

    /**
     * Get the name of the Army.
     *
     * @return this.name.
     */

    public String getName() {
        return name;
    }

    public Army deepCopy() {
        ArrayList<Unit> copy = new ArrayList<>();

        for (Unit unit : this.units) {
            copy.add(unit.copy());
        }

        return new Army(this.name, copy);
    }

    /**
     * Get how many units there are in an Army.
     *
     * @return int
     */

    public int size() {
        return units.size();
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
        return name.equals(army.name) && Objects.equals(this.sort(), army.sort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, this.sort());
    }
}