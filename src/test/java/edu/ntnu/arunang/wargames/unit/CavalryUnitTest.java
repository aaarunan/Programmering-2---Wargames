package edu.ntnu.arunang.wargames.unit;

import edu.ntnu.arunang.wargames.Terrain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CavalryUnitTest {

    CavalryUnit attacker = new CavalryUnit("attacker", 20);
    CavalryUnit defender = new CavalryUnit("defender", 15);

    @Test
    @DisplayName("Test attacking once")
    void testAttackOnce() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(2, defender.getHealthPoints());
    }

    @Test
    @DisplayName("Checking attack bonus after attacking once")
    void testAttackBonus() {
        int baseAttackBonus = CavalryUnit.BASE_ATTACK_BONUS;
        int attackBonus = CavalryUnit.FIRST_ATTACK_BONUS;

        attacker.attack(defender);

        assertEquals(baseAttackBonus, attacker.getAttackBonus());
        assertEquals(attackBonus, defender.getAttackBonus());
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
    @DisplayName("Test death of CavalryUnit (attacking twice)")
    void testDeath() {
        attacker.attack(defender);

        assertEquals(20, attacker.getHealthPoints());
        assertEquals(2, defender.getHealthPoints());

        attacker.attack(defender);

        //Unit should be dead
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(-7, defender.getHealthPoints());

        assertTrue(defender.isDead());
        assertFalse(attacker.isDead());
    }

    @Test
    @DisplayName("Test all fields are equal when copying when the Unit has been Attacked")
    void testCopy() {
        attacker.attack(defender);
        CavalryUnit copy = attacker.copy();
        assertEquals(attacker, copy);
    }

    @Test
    @DisplayName("Test attack bonus on all available terrains")
    void testAttackBonusOnTerrain() {
        for (Terrain terrain : Terrain.values()) {
            int attackBonus = 0;
            try {
                attackBonus = attacker.getAttackBonus(terrain);
                attacker.attack(defender, terrain);
            } catch (Exception e) {
                fail(String.format("Attack bonus: %d is not valid for terrain '%s'.", attackBonus, terrain));
                e.printStackTrace();
            }
        }
    }

    @Test
    @DisplayName("Test resist bonus on all available terrains")
    void testDefenceBonusOnTerrain() {
        for (Terrain terrain : Terrain.values()) {
            int resistBonus = 0;
            try {
                resistBonus = attacker.getAttackBonus(terrain);
                defender.attack(attacker, terrain);
            } catch (Exception e) {
                fail(String.format("Resist bonus %d is not valid for terrain '%s'.", resistBonus, terrain));
            }
        }
    }
}


