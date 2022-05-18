package edu.ntnu.arunang.wargames.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitFactoryTest {
    @Test
    @DisplayName("Test creating a CavalryUnit")
    void testOnCreatingCavalryUnit() {
        CavalryUnit cavalryUnit = new CavalryUnit("name", 10);
        CavalryUnit cavalryUnitFromFactory = (CavalryUnit) UnitFactory.constructUnitFromString("CavalryUnit", "name",
                10);

        assertEquals(cavalryUnit, cavalryUnitFromFactory);
    }

    @Test
    @DisplayName("Creating Unit that is not defined")
    void testOnNotDefined() {

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            UnitFactory.constructUnitFromString("NotAType", "name", 10);
        });

        assertEquals("Unittype NotAType does not exist", thrown.getMessage());
    }

    @Test
    @DisplayName("All unittypes should is able to be created")
    void testAllUnitTypes() {
        for (UnitType type : UnitType.values()) {
            try {
                UnitFactory.constructUnit(type, "name", 10);
            } catch (IllegalArgumentException e) {
                fail(e.getMessage());
            }
        }
    }

}