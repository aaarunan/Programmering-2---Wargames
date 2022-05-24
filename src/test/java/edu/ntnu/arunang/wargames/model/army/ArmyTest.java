package edu.ntnu.arunang.wargames.model.army;

import edu.ntnu.arunang.wargames.model.unit.*;
import edu.ntnu.arunang.wargames.model.unit.util.UnitType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArmyTest {


    CavalryUnit cavUnit = new CavalryUnit("cavUnit", 20);
    InfantryUnit infUnit = new InfantryUnit("infUnit", 40);
    CommanderUnit overPoweredUnit = new CommanderUnit("opUnit", 10000);

    CavalryUnit P1 = new CavalryUnit("A", 30);
    CavalryUnit P2 = new CavalryUnit("C", 20);
    InfantryUnit P4 = new InfantryUnit("C", 25);
    CommanderUnit P3 = new CommanderUnit("C", 20);
    CommanderUnit P5 = new CommanderUnit("C", 40);
    CommanderUnit P6 = new CommanderUnit("C", 40);
    RangedUnit P8 = new RangedUnit("D", 40);
    RangedUnit P7 = new RangedUnit("D", 40);

    @Test
    @DisplayName("Checks deletion when there are similar armies in the Army")
    void testRemoveSimilarUnit() {
        Army attacker = new Army("Attacker");
        Army defender = new Army("Defender");

        attacker.add(cavUnit);
        defender.add(cavUnit);
        defender.add(cavUnit);

        defender.remove(cavUnit);
        attacker.remove(cavUnit);

        assertFalse(attacker.hasUnits());
        assertTrue(defender.hasUnits());
    }

    @Test
    @DisplayName("Checks deletion for sub-class specific differences.")
    void testRemoveSpecificUnit() {
        Army attacker = new Army("Attacker");

        attacker.add(cavUnit, 2);
        Unit attackerUnit = attacker.get(0);

        attackerUnit.attack(cavUnit);
        attacker.remove(attackerUnit);

        assertNotEquals(attackerUnit, attacker.get(0));

    }

    @Test
    @DisplayName("Test that getRandom() works")
    void testGetRandom() {
        Army attacker = new Army("Attacker");

        int count = 100;
        int units = 0;

        attacker.add(cavUnit, count);

        while (attacker.hasUnits()) {
            Unit temp = attacker.getRandom();
            overPoweredUnit.attack(temp);
            attacker.remove(temp);
            units++;
        }

        assertEquals(count, units);
        assertFalse(attacker.hasUnits());
    }

    @Test
    @DisplayName("Check getRandom() on empty Army")
    void getRandomEmpty() {
        Army attacker = new Army("Attacker");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, attacker::getRandom,
                "Army has no units."

        );
    }

    @Test
    @DisplayName("Check getting a specific type of Unit, on matching units")
    void getUnitByType() {
        Army attacker = new Army("Attacker");
        attacker.add(infUnit);
        attacker.add(infUnit);
        attacker.add(overPoweredUnit);
        attacker.add(cavUnit);
        List<Unit> temp = attacker.getUnitsByType(UnitType.CommanderUnit);


        assertEquals(temp.get(0), overPoweredUnit);
        assertEquals(1, temp.size());

    }

    @Test
    @DisplayName("Check getting a specific type of Unit, when no unit match")
    void getUnitByUnitClass() {
        Army attacker = new Army("Attacker");
        List<Unit> temp = attacker.getUnitsByType(UnitType.RangedUnit);

        attacker.add(infUnit);
        attacker.add(infUnit);
        attacker.add(overPoweredUnit);
        attacker.add(cavUnit);

        assertEquals(0, temp.size());
    }

    @Test
    @DisplayName("Check sorting if it matches a pre sorted Army.")
    void testSortOnPreSortedArmy() {
        Army unitsUnsorted = new Army("unsorted");
        ArrayList<Unit> unitsSorted = new ArrayList<>();

        unitsSorted.add(P1);
        unitsSorted.add(P2);
        unitsSorted.add(P3);
        unitsSorted.add(P4);
        unitsSorted.add(P5);
        unitsSorted.add(P6);

        unitsUnsorted.add(P2);
        unitsUnsorted.add(P5);
        unitsUnsorted.add(P1);
        unitsUnsorted.add(P4);
        unitsUnsorted.add(P3);
        unitsUnsorted.add(P6);

        assertEquals(unitsSorted, unitsUnsorted.sortUnits());

    }

    @Test
    @DisplayName("Check sorting if it matches a pre sorted with different attack bonuses in an Army.")
    void testSortOnPreSortedArmyWithUniqueAttackBonus() {
        Army unitsUnsorted = new Army("unsorted");
        ArrayList<Unit> unitsSorted = new ArrayList<>();

        P6.attack(cavUnit);
        cavUnit.attack(P7);

        unitsSorted.add(P1);
        unitsSorted.add(P2);
        unitsSorted.add(P3);
        unitsSorted.add(P4);
        unitsSorted.add(P7);
        unitsSorted.add(P8);

        unitsUnsorted.add(P8);
        unitsUnsorted.add(P2);
        unitsUnsorted.add(P1);
        unitsUnsorted.add(P4);
        unitsUnsorted.add(P3);
        unitsUnsorted.add(P7);

        assertEquals(unitsSorted, unitsUnsorted.sortUnits());
    }

    @Test
    @DisplayName("Testing sort on empty army")
    void testSortOnEmpty() {
        Army army = new Army("test");

        assertEquals(new ArrayList<Unit>(), army.sortUnits());
    }

    @Test
    @DisplayName("Testing total attack")
    void testTotalAttack() {
        Army army = new Army("test");

        army.add(new CavalryUnit("yo", 10), 10);

        assertEquals(200, army.getTotalAttackPoints());
    }

    @Test
    @DisplayName("Test the total of armor points")
    void testTotalArmorPoints() {
        Army army = new Army("test");

        army.add(new CavalryUnit("yo", 10), 10);

        assertEquals(120, army.getTotalArmorPoints());
    }

    @Test
    @DisplayName("Test getting map on two different units")
    void testGetMap() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        CavalryUnit cavalryUnit = new CavalryUnit("cav", 10);
        InfantryUnit infantryUnit = new InfantryUnit("inf", 10);

        army.add(cavalryUnit, 10);
        army.add(infantryUnit, 10);

        expectedMap.put(cavalryUnit, 10);
        expectedMap.put(infantryUnit, 10);

        assertEquals(expectedMap, army.getMap());
    }

    @Test
    @DisplayName("Test getting map on different healthpoints")
    void testGetMapOnDifferentHealthPoints() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        CavalryUnit cavUnit1 = new CavalryUnit("cav", 10);
        CavalryUnit cavUnit2 = new CavalryUnit("cav", 11);

        army.add(cavUnit1, 10);
        army.add(cavUnit2, 10);

        expectedMap.put(cavUnit1, 10);
        expectedMap.put(cavUnit2, 10);

        assertEquals(expectedMap, army.getMap());
    }

    @Test
    @DisplayName("Test getting map on non sorted")
    void testGetMapOnNonSorted() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        CavalryUnit cavUnit1 = new CavalryUnit("cav", 10);
        CavalryUnit cavUnit2 = new CavalryUnit("cav", 11);

        army.add(cavUnit1, 10);
        army.add(cavUnit2, 10);
        army.add(cavUnit1, 10);

        expectedMap.put(cavUnit1, 20);
        expectedMap.put(cavUnit2, 10);

        assertEquals(expectedMap, army.getMap());
    }

    @Test
    @DisplayName("Test get map on empty")
    void testGetMapOnEmpty() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        assertEquals(expectedMap, army.getMap());
    }

    @Test
    @DisplayName("Test getting condensed map on two different units")
    void testGetMapCondensed() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        CavalryUnit cavalryUnit = new CavalryUnit("cav", 10);
        InfantryUnit infantryUnit = new InfantryUnit("inf", 10);

        army.add(cavalryUnit, 10);
        army.add(infantryUnit, 10);

        cavalryUnit.setHealthPoints(1);
        infantryUnit.setHealthPoints(1);

        expectedMap.put(cavalryUnit, 10);
        expectedMap.put(infantryUnit, 10);

        assertEquals(expectedMap, army.getCondensedMap());
    }

    @Test
    @DisplayName("Test getting condensed map on different healthpoints")
    void testGetCondensedMapOnDiffrentHealthPoints() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        CavalryUnit cavUnit1 = new CavalryUnit("cav", 10);
        CavalryUnit cavUnit2 = new CavalryUnit("cav", 11);

        army.add(cavUnit1, 10);
        army.add(cavUnit2, 10);

        cavUnit1.setHealthPoints(1);

        expectedMap.put(cavUnit1, 20);

        assertEquals(expectedMap, army.getCondensedMap());
    }

    @Test
    @DisplayName("Test getting condensed map on non sorted")
    void testGetCondensedMapOnNonSorted() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        CavalryUnit cavUnit1 = new CavalryUnit("cav", 10);
        CavalryUnit cavUnit2 = new CavalryUnit("cav", 11);
        InfantryUnit infantryUnit = new InfantryUnit("inf", 10);

        army.add(cavUnit1, 10);
        army.add(cavUnit2, 10);
        army.add(infantryUnit, 10);
        army.add(cavUnit1, 10);

        cavUnit1.setHealthPoints(1);
        infantryUnit.setHealthPoints(1);

        expectedMap.put(cavUnit1, 30);
        expectedMap.put(infantryUnit, 10);

        assertEquals(expectedMap, army.getCondensedMap());
    }

    @Test
    @DisplayName("Test get condensed map on empty")
    void testGetCondensedMapOnEmpty() {
        Army army = new Army("test");
        HashMap<Unit,Integer> expectedMap = new HashMap<>();

        assertEquals(expectedMap, army.getCondensedMap());
    }
}