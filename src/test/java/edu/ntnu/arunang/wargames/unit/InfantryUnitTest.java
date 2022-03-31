package edu.ntnu.arunang.wargames.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfantryUnitTest {

    InfantryUnit attacker = new InfantryUnit("attacker", 20);
    InfantryUnit defender = new InfantryUnit("defender", 15);

    @Test
    @DisplayName("Test attacking once")
    void testAttack() {
        attacker.attack(defender);
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(9, defender.getHealthPoints());

    }

    @Test
    @DisplayName("Test death of Unit")
    void testDeath() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(9, defender.getHealthPoints());

        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(3, defender.getHealthPoints());


        attacker.attack(defender);

        //edu.ntnu.arunang.wargames.Unit should be dead
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(-3, defender.getHealthPoints());

        assertTrue(defender.isDead());
        assertFalse(attacker.isDead());
    }

    @Test
    @DisplayName("Test all fields on copying are equal when unit has attacked")
    void testCopy() {
        attacker.attack(defender);
        InfantryUnit copy = attacker.copy();
        assertEquals(attacker, copy);
    }

}

