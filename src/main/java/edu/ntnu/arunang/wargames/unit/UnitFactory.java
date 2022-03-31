package edu.ntnu.arunang.wargames.unit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This is a Factory class that creates Units from strings. This is usually done
 * when reading from files. When adding new Units, they should also be added to the factory.
 * This class is private because it only has static methods and variables, and there is therefore no point
 * to it being instantiated.
 */

public class UnitFactory {

    //Types of units which are parsable
    private static final String CAVALRY_UNIT = "CavalryUnit";
    private static final String INFANTRY_UNIT = "InfantryUnit";
    private static final String RANGED_UNIT = "RangedUnit";
    private static final String COMMANDER_UNIT = "CommanderUnit";

    /**
     * Private constructor because the class should not be instantiated.
     */

    private UnitFactory() {
    }

    /**
     * Constructs a unit from a parsed line. The army is created with stats reset.
     *
     * @param type   unit type
     * @param name   unit name
     * @param health unit health
     * @return A constructed Unit.
     * @throws IllegalStateException if the Unit type is not defined or wrong.
     */

    public static Unit constructUnitFromString(String type, String name, int health) throws IllegalStateException {
        return switch (type) {
            case CAVALRY_UNIT -> new CavalryUnit(name, health);
            case COMMANDER_UNIT -> new CommanderUnit(name, health);
            case INFANTRY_UNIT -> new InfantryUnit(name, health);
            case RANGED_UNIT -> new RangedUnit(name, health);
            default -> throw new IllegalArgumentException(String.format("Unittype %s does not exist", type));
        };
    }

    public static boolean typeExists(String type) {
        try {
            UnitFactory.constructUnitFromString(type, "name", 10);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Constructs a unit from a parsed line. The army is created with stats reset.
     * This is a prototype, and is in testing phase. Might be a better solution.
     *
     * @param type   unit type
     * @param name   unit name
     * @param health unit health
     * @return A constructed Unit.
     * @throws IllegalStateException if the Unit type is not defined or wrong.
     */

    public static Unit constructUnitFromStringv2(String type, String name, int health) {
        Unit instance;
        try {
            Class<?> clazz = Class.forName("edu.ntnu.arunang.wargames.unit." + type);
            Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
            instance = (Unit) constructor.newInstance(name, health);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("The given unit type does not exist");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("A constructor for the given Unit does not exist");
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("Could not construct the Unit");
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Could not instantiate the Unit");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Could not access the constructor of the Unit");
        }
        return instance;
    }
}
