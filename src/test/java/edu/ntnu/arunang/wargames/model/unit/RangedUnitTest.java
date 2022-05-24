package edu.ntnu.arunang.wargames.model.unit;

import edu.ntnu.arunang.wargames.model.battle.Terrain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        attacker.attack(defender);
        attacker.attack(defender);

        // player should be dead
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(0, defender.getHealthPoints());

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
