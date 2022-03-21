package edu.ntnu.arunang.wargames.Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitFactoryTest {
    @Test
    @DisplayName("Test creating a CavalryUnit")
    void testOnCreatingCavalryUnit() {
        UnitFactory unitFactory = new UnitFactory();
        CavalryUnit cavalryUnit = new CavalryUnit("name", 10);
        CavalryUnit cavalryUnitFromFactory = (CavalryUnit) unitFactory.constructUnitFromString("CavalryUnit", "name", 10);

        assertEquals(cavalryUnit, cavalryUnitFromFactory);
    }

    @Test
    @DisplayName("Creating Unit that is not defined")
    void testOnNotDefined() {
       UnitFactory unitFactory = new UnitFactory();

       try {
           unitFactory.constructUnitFromString("NotAType", "name", 10);
       } catch (Exception e) {
           assertEquals(IllegalArgumentException.class, e.getClass());
       }
    }

}