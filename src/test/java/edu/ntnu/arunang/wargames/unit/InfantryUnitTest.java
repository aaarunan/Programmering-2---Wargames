package edu.ntnu.arunang.wargames.unit;

import edu.ntnu.arunang.wargames.Terrain;
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

