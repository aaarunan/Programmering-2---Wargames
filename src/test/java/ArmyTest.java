import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Unit.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArmyTest {

    Army attacker = new Army("Attacker");
    Army defender = new Army("Defender");


    CavalryUnit unit = new CavalryUnit("unit", 20);
    InfantryUnit unit1 = new InfantryUnit("unit1", 40);
    CommanderUnit opUnit = new CommanderUnit("opUnit", 10000);

    @Test
    void addAll() {

        ArrayList<Unit> units = new ArrayList<>();
        units.add(unit);
        units.add(unit1);
        attacker.add(units);

        for (int i = 0; i < units.size(); i++) {
            assertEquals(units.get(i), attacker.get(i));
        }
    }

    @Test
    void testRemove() {
        attacker.add(unit);
        defender.add(unit);

        assertTrue(defender.remove(unit));

        assertTrue(attacker.hasUnits());
        assertFalse(defender.hasUnits());
    }

    @Test
    @DisplayName("hasUnits() must be true if there are units in the army")
    void testHasUnits() {
        attacker.add(unit);

        assertTrue(attacker.hasUnits());
        assertFalse(defender.hasUnits());
    }

    @Test
     void getRandom() {
        int units = 6;

        while (units > 0) {
            attacker.add(unit);
            units--;
        }

        while (attacker.hasUnits()) {
            Unit temp = attacker.getRandom();
            opUnit.attack(temp);
            attacker.remove(temp);
            units++;
        }

        assertEquals(6, units);
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
}