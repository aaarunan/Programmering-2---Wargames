package edu.ntnu.arunang.wargames.model.unit;

/**
 * List of all unit-types. These unit-types are used in the UnitFactory to create a Unit. They are also used to get the
 * full list of unit-types.
 */

public enum UnitType {
    CavalryUnit, CommanderUnit, InfantryUnit, RangedUnit;

    /**
     * Helper method to check if the specified unit is an UnitType. ValueOf is a static method from the enum class and
     * can therefor not be overridden. Use this instead of valueOf.
     *
     * @param string that is parsed
     * @return UnitType
     * @throws IllegalArgumentException if the UnitType does not exist.
     */

    public static UnitType getUnitType(String string) {

        UnitType unitType;
        try {
            unitType = valueOf(string);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Unittype %s does not exist", string));
        }
        return unitType;
    }

}
