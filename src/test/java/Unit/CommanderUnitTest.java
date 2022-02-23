package Unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CommanderUnitTest {

    CommanderUnit attacker = new CommanderUnit("attacker", 20);
    CommanderUnit defender = new CommanderUnit("defender", 20);

    @Test
    @DisplayName("Construct CommanderUnit wrong.")
    void testConstructWrong() {

    }

    @Test
    @DisplayName("Attacking once")
    void testAttack() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(5, defender.getHealthPoints());

    }

    @Test
    @DisplayName("Test attack bonus after attacking once")
    void testAttackBonus() {
        int baseAttackBonus = CommanderUnit.BASE_ATTACK_BONUS;
        int attackBonus = CommanderUnit.FIRST_ATTACK_BONUS+CommanderUnit.BASE_ATTACK_BONUS;

        assertEquals(attackBonus, attacker.getAttackBonus());
        assertEquals(attackBonus, defender.getAttackBonus());

        attacker.attack(defender);

        assertEquals(baseAttackBonus, attacker.getAttackBonus());
        assertEquals(attackBonus, defender.getAttackBonus());
    }


    @Test
    @DisplayName("Test attackbonus after attacking twice")
    void testAttackBonusTwice() {
        int baseAttackBonus = CavalryUnit.BASE_ATTACK_BONUS;
        int attackBonus = CavalryUnit.BASE_ATTACK_BONUS + CavalryUnit.FIRST_ATTACK_BONUS;

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

        //Unit should be dead
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(-6, defender.getHealthPoints());

        assertTrue(defender.isDead());
        assertFalse(attacker.isDead());
    }
}

