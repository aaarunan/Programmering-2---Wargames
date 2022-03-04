package edu.ntnu.arunang.wargames;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import edu.ntnu.arunang.wargames.Unit.*;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class ArmyTest {

    Army attacker = new Army("Attacker");
    Army defender = new Army("Defender");

    CavalryUnit cavUnit = new CavalryUnit("unit", 20);
    InfantryUnit infUnit = new InfantryUnit("unit1", 40);
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
    @DisplayName("Checks if wrong construction with name and units parameter. ")
    void testWrongConstructionOnMainConstruction() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> new Army(""),
                "Name must not be blank."

        );

    }

    @Test
    @DisplayName("Checks if wrong construction with nameparameter. ")
    void testWrongConstructionOnSecondaryConstructor() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> new Army("", new ArrayList<Unit>()),
                "Name must not be blank."

        );

    }

    @DisplayName("Checks deletion when there are similar armies in the edu.ntnu.arunang.wargames.Army")
    void testRemoveSimilarUnit() {
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
        attacker.add(cavUnit, 2);

        Unit attackerUnit = attacker.get(0);
        attackerUnit.attack(cavUnit);
        attacker.remove(attackerUnit);

        assertNotEquals(attackerUnit, attacker.get(0));

    }

    @Test
    @DisplayName("Test that getRandom works")
    void testGetRandom() {
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
    @DisplayName("Check getRandom() on empty edu.ntnu.arunang.wargames.Army")
    void getRandomEmpty() {
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> attacker.getRandom(),
                "edu.ntnu.arunang.wargames.Army has no units."

        );
    }

    @Test
    @DisplayName("Check sorting if it matches a pre sorted edu.ntnu.arunang.wargames.Army.")
    void testSortOnPreSortedArmy() {
        Army unitsUnsorted = new Army("unsorted");
        ArrayList<Unit> unitsSorted = new ArrayList();

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

        assertEquals(unitsSorted, unitsUnsorted.sort());

    }

    @Test
    @DisplayName("Check sorting if it matches a pre sorted with different attackBonuses edu.ntnu.arunang.wargames.Army.")
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

        assertEquals(unitsSorted, unitsUnsorted.sort());

    }
    @Test
    @DisplayName("Testing sort on empty army")
    void testSortOnEmpty() {
        Army army = new Army("test");

        assertEquals(new ArrayList<Unit>(), army.sort());
    }
}