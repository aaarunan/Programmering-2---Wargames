import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Unit.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArmyTest {

    Army attacker = new Army("Attacker");
    Army defender = new Army("Defender");


    @Test
    void addAll() {
        CavalryUnit unit = new CavalryUnit("unit", 20);
        InfantryUnit unit1 = new InfantryUnit("unit1", 40);

        ArrayList<Unit> units = new ArrayList<>();

        units.add(unit);
        units.add(unit1);
        attacker.addAll(units);

        for (int i = 0; i < units.size(); i++) {
            assertEquals(units.get(i), attacker.get(i));
        }
    }

    @Test
    void testRemove() {
        CavalryUnit unit = new CavalryUnit("unit", 20);

        attacker.add(unit);
        defender.add(unit);
        defender.remove(unit);

        assertTrue(attacker.hasUnits());
        assertFalse(defender.hasUnits());
    }

    @Test
    @DisplayName("hasUnits() must be true if there are units in the army")
    void testHasUnits() {
        CavalryUnit unit = new CavalryUnit("unit", 20);

        attacker.add(unit);

        assertTrue(attacker.hasUnits());
        assertFalse(defender.hasUnits());
    }

    @Test
    void getRandom() {

    }
}