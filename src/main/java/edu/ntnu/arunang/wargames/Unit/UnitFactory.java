package edu.ntnu.arunang.wargames.Unit;

/**
 * This is a Factory class that creates Units from strings. This is usually done
 * when reading from files. When adding new Units, they should also be added to the factory.
 */

public class UnitFactory {

    //Types of units which are parsable
    private final String CAVALRY_UNIT = "CavalryUnit";
    private final String INFANTRY_UNIT = "InfantryUnit";
    private final String RANGED_UNIT = "RangedUnit";
    private final String COMMANDER_UNIT = "CommanderUnit";

    /**
     * Constructs a unit from a parsed line. The army is created with stats reset.
     *
     * @param type   unit type
     * @param name   unit name
     * @param health unit health
     * @return A constructed Unit.
     * @throws IllegalStateException if the Unit type is not defined or wrong.
     */

    public Unit constructUnitFromString(String type, String name, int health) throws IllegalStateException {
        return switch (type) {
            case CAVALRY_UNIT -> new CavalryUnit(name, health);
            case COMMANDER_UNIT -> new CommanderUnit(name, health);
            case INFANTRY_UNIT -> new InfantryUnit(name, health);
            case RANGED_UNIT -> new RangedUnit(name, health);
            default -> throw new IllegalArgumentException(String.format("Unittype %s does not exist.", type));
        };
    }
}
