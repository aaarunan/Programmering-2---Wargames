package edu.ntnu.arunang.wargames.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UnitFactoryTest {
    @Test
    @DisplayName("Test creating a CavalryUnit")
    void testOnCreatingCavalryUnit() {
        CavalryUnit cavalryUnit = new CavalryUnit("name", 10);
        CavalryUnit cavalryUnitFromFactory = (CavalryUnit) UnitFactory.constructUnitFromStringv2("CavalryUnit", "name", 10);

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

}