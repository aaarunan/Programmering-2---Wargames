package org.ntnu.vsbugge.wargames.units;

import Unit.CavalryUnit;
import junit.framework.TestCase;

public class CavalryUnitTest extends TestCase {

    CavalryUnit attacker = new CavalryUnit("Test1", 20);
    CavalryUnit defender = new CavalryUnit("Test2", 15);

    public void testAttack() {

        attacker.attack(defender);
        assertEquals(attacker.getHealthPoints(), 20);
        assertEquals(defender.getHealthPoints(), 12);

        attacker.attack(defender);
        assertEquals(attacker.getHealthPoints(), 20);
        assertEquals(defender.getHealthPoints(), 12);

        attacker.attack(defender);
        assertEquals(defender.getHealthPoints(), 12);

        assertEquals(attacker.getHealthPoints(), 20);

        defender.attack(attacker);
        assertEquals(attacker.getHealthPoints(), 0);
        assertEquals(defender.getHealthPoints(), 12);

    }

    public void testGetAttack() {
        assertEquals(attacker.getAttackPoints(), 10);
        assertEquals(defender.getAttackPoints(), 20);
    }

    public void testGetArmor() {
        assertEquals(attacker.getArmorPoints(), 5);
        assertEquals(defender.getArmorPoints(), 12);
    }

    public void testTakeDamage() {
        attacker.setHealthPoints(2);
        assertEquals(attacker.getHealthPoints(), 13);
    }

    public void testToString() {
        assertEquals(attacker.toString(), "CavalryUnit{name='Test1', health=20, attack=10, armor=5}");
    }
}