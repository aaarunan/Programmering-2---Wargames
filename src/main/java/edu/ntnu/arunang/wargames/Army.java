package edu.ntnu.arunang.wargames;

import edu.ntnu.arunang.wargames.Unit.*;

import java.util.*;

/**
 * An Army is a collection of Units.
 * It has a name that defines it.
 * <p>
 * The Army class uses an ArrayList for storing the Units.
 * This is because there can be duplicates of the same troop in the Army,
 * and the order of the Units does not matter.
 */

public class Army {

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
     * Used for testing purposes and easily adding multiples Units.
     *
     * @param unit  Unit that is added
     * @param count Amount of Units that should be added.
     */
    protected void add(Unit unit, int count) {
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
     * Usually done if the Unit is dead.
     *
     * @param unit the Unit that is being removed.
     * @return true if successful, false if unsuccessful.
     */

    public boolean remove(Unit unit) {
        return units.remove(unit);
    }

    /**
     * Checks if there are Units in the Army.
     *
     * @return false if empty, true if count > 0.
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

    /**
     * Used for creating antoher instance of the same Army.
     * Copies all units and put them in an ArrayList.
     * Usually done before sorting, and for testing purposes.
     *
     * @return Arraylist of all the units in the Army.
     */

    protected ArrayList<Unit> deepCopy() {
        ArrayList<Unit> copy = new ArrayList<>();

        for (Unit unit : this.units) {
            copy.add(unit.copy());
        }
        return copy;
    }

    /**
     * Sorts the Army given by the Unit's compareTo method.
     * It copies the Army and puts all the Units in an Arraylist.
     *
     * @return a copy of a sorted Army.
     */

    protected ArrayList<Unit> sort() {
        ArrayList<Unit> copy = this.deepCopy();
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

    /**
     * The Army is sorted when checkings equals because the order
     * does not matter when checking if two armies are equal.
     *
     * @param o
     * @return true if equal, false if unequal
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Army)) return false;
        Army army = (Army) o;
        return name.equals(army.name) && Objects.equals(this.sort(), army.sort());
    }

    /**
     * When the army gets hashed, the order of the Army
     * does not matter, therefore the Army is sorted before hashing.
     *
     * @return hash of the Army.
     */

    @Override
    public int hashCode() {
        return Objects.hash(name, this.sort());
    }
}