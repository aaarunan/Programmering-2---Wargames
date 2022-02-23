package Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfantryUnitTest {

    InfantryUnit attacker = new InfantryUnit("attacker", 20);
    InfantryUnit defender = new InfantryUnit("defender", 15);

    @Test
    @DisplayName("Constructing InfantryUnit wrong")
    void testWrongConstruction() {

    }

    @Test
    @DisplayName("Test attacking once")
    void testAttack() {
        attacker.attack(defender);
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(9, defender.getHealthPoints());

    }

    @Test
    @DisplayName("Testing death functionality")
    void testDeath() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(9, defender.getHealthPoints());

        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(3, defender.getHealthPoints());


        attacker.attack(defender);

        //Unit should be dead
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(-3, defender.getHealthPoints());

        assertTrue(defender.isDead());
        assertFalse(attacker.isDead());
    }


}

