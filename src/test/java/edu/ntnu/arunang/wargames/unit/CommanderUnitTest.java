package edu.ntnu.arunang.wargames.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CommanderUnitTest {

    CommanderUnit attacker = new CommanderUnit("attacker", 20);
    CommanderUnit defender = new CommanderUnit("defender", 20);

    @Test
    @DisplayName("Test attacking once")
    void testAttack() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(5, defender.getHealthPoints());

    }

    @Test
    @DisplayName("Test attack bonus after attacking once")
    void testAttackBonus() {
        int baseAttackBonus = CommanderUnit.BASE_ATTACK_BONUS;
        int firstAttackBonus = CommanderUnit.FIRST_ATTACK_BONUS;

        assertEquals(firstAttackBonus, attacker.getAttackBonus());
        assertEquals(firstAttackBonus, defender.getAttackBonus());

        attacker.attack(defender);

        assertEquals(baseAttackBonus, attacker.getAttackBonus());
        assertEquals(firstAttackBonus, defender.getAttackBonus());
    }


    @Test
    @DisplayName("Test attack bonus after attacking twice")
    void testAttackBonusTwice() {
        int baseAttackBonus = CavalryUnit.BASE_ATTACK_BONUS;
        int attackBonus = CavalryUnit.FIRST_ATTACK_BONUS;

        attacker.attack(defender);
        attacker.attack(defender);

        assertEquals(baseAttackBonus, attacker.getAttackBonus());
        assertEquals(attackBonus, defender.getAttackBonus());
    }

    @Test
    @DisplayName("Testing death of CommanderUnit")
    void testDeath() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(5, defender.getHealthPoints());

        attacker.attack(defender);

        //edu.ntnu.arunang.wargames.Unit should be dead
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(-6, defender.getHealthPoints());

        assertTrue(defender.isDead());
        assertFalse(attacker.isDead());
    }

    @Test
    @DisplayName("Test all fields on copying are equal when unit has attacked")
    void testCopy() {
        attacker.attack(defender);
        CommanderUnit copy = attacker.copy();
        assertEquals(attacker, copy);
    }
}

