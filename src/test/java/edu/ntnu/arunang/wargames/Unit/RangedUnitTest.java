package edu.ntnu.arunang.wargames.Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ranges.Range;

import static org.junit.jupiter.api.Assertions.*;

class RangedUnitTest {

    RangedUnit attacker = new RangedUnit("Test1", 20);
    RangedUnit defender = new RangedUnit("Test2", 15);

    @Test
    @DisplayName("Test attacking once")
    void testAttack() {
        attacker.attack(defender);
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(11, defender.getHealthPoints());

    }

    @Test
    @DisplayName("Test resist bonus when Unit has been hit")
    void testResistBonus() {
        int increment = RangedUnit.RESIST_INTERVAL;
        int resistBonus = 3 * RangedUnit.BASE_RESIST_BONUS;

        while (resistBonus > RangedUnit.BASE_RESIST_BONUS) {
            assertEquals(resistBonus, defender.getResistBonus());
            attacker.attack(defender);
            resistBonus = resistBonus - increment;
        }
    }

    @Test
    @DisplayName("Test death of the Unit")
    void testDeath() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(11, defender.getHealthPoints());

        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(5, defender.getHealthPoints());

        attacker.attack(defender);

        //player should be dead
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(-3, defender.getHealthPoints());

        assertTrue(defender.isDead());
        assertFalse(attacker.isDead());
    }

    @Test
    @DisplayName("Test all fields on copying are equal when unit has attacked")
    void testCopy() {
        attacker.attack(defender);
        RangedUnit copy = attacker.copy();
        assertEquals(attacker, copy);
    }

}

