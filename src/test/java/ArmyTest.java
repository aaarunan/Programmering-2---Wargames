import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Unit.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArmyTest {

    Army attacker = new Army("Attacker");
    Army defender = new Army("Defender");

    CavalryUnit cavUnit = new CavalryUnit("unit", 20);
    InfantryUnit infUnit = new InfantryUnit("unit1", 40);
    CommanderUnit overPoweredUnit = new CommanderUnit("opUnit", 10000);

    @Test
    @DisplayName("Checks deletion when there are similar armies in the Army")
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
    @DisplayName("Checks deletion for character specific differences.")
    void testRemoveSpecificUnit() {
        attacker.add(cavUnit, 2);

        Unit attackerUnit = attacker.get(0);
        attackerUnit.attack(cavUnit);
        attacker.remove(attackerUnit);

        assertNotEquals(attackerUnit, attacker.get(0));

    }

    @Test
    @DisplayName("Test that getRandom works")
    void getRandom() {
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
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> attacker.getRandom(),
                "Army has no units."

        );
    }

    @Test
    @DisplayName("Check sorting")
    void testSort() {
        CavalryUnit cUnit = new CavalryUnit("A", 30);
        CavalryUnit cUnit1 = new CavalryUnit("C", 20);
        InfantryUnit iUnit = new InfantryUnit("A", 20);
        CommanderUnit comUnit = new CommanderUnit("C", 20);

        Army unitsUnsorted = new Army("unsorted");
        ArrayList<Unit> unitsSorted = new ArrayList();

        unitsSorted.add(iUnit);
        unitsSorted.add(cUnit);
        unitsSorted.add(cUnit1);
        unitsSorted.add(comUnit);

        unitsUnsorted.add(cUnit);
        unitsUnsorted.add(iUnit);
        unitsUnsorted.add(comUnit);
        unitsUnsorted.add(cUnit1);

        assertEquals(unitsSorted, unitsUnsorted.sort());
    }

    @Test
    @DisplayName("Testing sort on empty army")
    void testSortOnEmpty() {
        Army army = new Army("test");

        assertEquals(new ArrayList<Unit>(), army.sort());
    }
}