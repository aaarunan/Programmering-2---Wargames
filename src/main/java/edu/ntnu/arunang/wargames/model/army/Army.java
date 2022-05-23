package edu.ntnu.arunang.wargames.model.army;

import edu.ntnu.arunang.wargames.model.unit.Unit;
import edu.ntnu.arunang.wargames.model.unit.UnitType;

import java.util.*;

/**
 * An Army is a collection of Units. It has a name that defines it.
 * <p>
 * The Army class uses an ArrayList for storing the Units. This is because there can be duplicates of the same troop in
 * the Army, and the order of the Units does not matter.
 */

public class Army {

    private final ArrayList<Unit> units;
    private final Random random = new Random(); // Used to get a random Unit
    private String name;

    /**
     * Constructs the Army with an empty ArrayList.
     *
     * @param name must not be empty.
     * @throws IllegalArgumentException if the name is blank.
     */

    public Army(String name) throws IllegalArgumentException {
        //check if name is blank
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty");
        }
        setName(name);
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
        //check if name is blank
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty");
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
     * Get the all specific types of Unit. The method uses generics so that each Unit does not need their own method.
     *
     * @param type The unit type
     * @return List of matching Units.
     */

    public List<Unit> getUnitsByType(UnitType type) {
        return units.stream().filter(e -> UnitType.getUnitType(e.getClass().getSimpleName()).equals(type)).toList();
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
     * Adding multiples of the same Unit. Used for testing purposes and easily adding multiples Units.
     *
     * @param unit  Unit that is added
     * @param count Amount of Units that should be added.
     */
    public void add(Unit unit, int count) {
        for (int i = 0; i < count; i++) {
            add(unit.copy());
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
     * Remove a Unit in the Army. Usually done if the Unit is dead.
     *
     * @param unit the Unit that is being removed.
     */

    public void remove(Unit unit) {
        units.remove(unit);
    }

    /**
     * Removes all the dead units from the army.
     */

    public void removeAllDeadUnits() {
        shallowCopyUnits().stream().filter(Unit::isDead).forEach(this::remove);
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
     */

    public Unit getRandom() {
        return units.get(random.nextInt(units.size()));
    }

    /**
     * Get the average healthpoints of the units.
     *
     * @return double containing the average healthpoints
     */

    public int getTotalHealthPoints() {
        return shallowCopyUnits().stream().mapToInt(Unit::getHealthPoints).sum();
    }

    /**
     * Get the average healthpoints of the units.
     *
     * @return double containing the average healthpoints
     */

    public int getTotalAttackPoints() {
        return shallowCopyUnits().stream().mapToInt(Unit::getAttackPoints).sum();
    }

    /**
     * Get the average healthpoints of the units.
     *
     * @return double containing the average healthpoints
     */

    public int getTotalArmorPoints() {
        return shallowCopyUnits().stream().mapToInt(Unit::getArmorPoints).sum();
    }

    /**
     * Used for creating another instance of the same Army. Copies all units and put them in an ArrayList. Usually done
     * before sorting, and for testing purposes.
     *
     * @return Arraylist of all the units in the Army.
     */

    protected ArrayList<Unit> deepCopyUnits() {
        ArrayList<Unit> copy = new ArrayList<>();

        for (Unit unit : this.units) {
            copy.add(unit.copy());
        }
        return copy;
    }

    /**
     * Used for creating another instance of the same Army. Copies all units and put them in an ArrayList. Usually done
     * before sorting, and for testing purposes.
     *
     * @return Arraylist of all the units in the Army.
     */

    public Army copy() {
        return new Army(this.getName(), this.deepCopyUnits());
    }

    /**
     * Get a shallow copy of the units. The units are the same, put in a new arraylist.
     *
     * @return a shallow copy of the units
     */

    public ArrayList<Unit> shallowCopyUnits() {
        return new ArrayList<>(this.units);
    }

    /**
     * Get a List of the Units. The army is converted to an ArrayList. The units are copied.
     *
     * @return List of units.
     */
    public ArrayList<Unit> getUnits() {
        return this.deepCopyUnits();
    }

    /**
     * Sorts the Army given by the Unit's compareTo method. It copies the Army and puts all the Units in an Arraylist.
     *
     * @return a copy of a sorted Army.
     */

    public List<Unit> sortUnits() {
        return units.stream().sorted().toList();
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
     * Set the name of the army
     *
     * @param name - the new name
     */

    public void setName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name can not be blank.");
        }
        this.name = name;
    }

    /**
     * Get how many units there are in an Army.
     *
     * @return int
     */

    public int size() {
        return units.size();
    }

    /**
     * Converts an Army into a map. This allows to get armies in a compact form with an Integer value that represents
     * count. Used to save armies efficiently in csv files. The units are copied and reset before converting.
     *
     * @return a hashmap of the army.
     */

    public Map<Unit, Integer> getMap() {
        Map<Unit, Integer> army = new HashMap<>();
        this.shallowCopyUnits().forEach(unit -> army.merge(unit.getResetCopy(), 1, Integer::sum));
        return army;
    }

    /**
     * Converts an Army into a map. This allows to get armies in a
     * compact form with an Integer value that represents
     * count. Unlike a normal map the healthpoints is set the 1,
     * generalizing the map. The units are copied and reset before converting.
     *
     * @return a hashmap of the army.
     */

    public Map<Unit, Integer> getCondensedMap() {
        Map<Unit, Integer> army = new HashMap<>();
        for (Unit unit : this.shallowCopyUnits()) {
            Unit copy = unit.getResetCopy();
            copy.setHealthPoints(1);
            army.merge(copy, 1, Integer::sum);
        }

        return army;
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
     * The Army is sorted when checking equals because the order
     * does not matter when checking if two armies are equal.
     *
     * @param o object that is being compared
     * @return true if equal, false if unequal
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Army army)) return false;
        return name.equals(army.name) && Objects.equals(this.sortUnits(), army.sortUnits());
    }

    /**
     * When the army gets hashed, the order of the Army does not matter, therefore the Army is sorted before hashing.
     *
     * @return hash of the Army.
     */

    @Override
    public int hashCode() {
        return Objects.hash(name, this.sortUnits());
    }
}