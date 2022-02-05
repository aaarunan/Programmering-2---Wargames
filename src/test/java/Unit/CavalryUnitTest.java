package org.ntnu.vsbugge.wargames.units;

import Unit.CavalryUnit;
import junit.framework.TestCase;

public class CavalryUnitTest extends TestCase {

    public void testAttack() {
        CavalryUnit test1 = new CavalryUnit("Test1", 20, 10, 5);
        CavalryUnit test2 = new CavalryUnit("Test2", 15);

        test1.attack(test2);
        assertEquals(test1.getHealthPoints(), 20);
        assertEquals(test2.getHealthPoints(), 12);

        test1.attack(test2);
        assertEquals(test1.getHealthPoints(), 20);
        assertEquals(test2.getHealthPoints(), 12);

        test1.attack(test2);
        assertEquals(test2.getHealthPoints(), 12);

        assertEquals(test1.getHealthPoints(), 20);

        test2.attack(test1);
        assertEquals(test1.getHealthPoints(), 0);
        assertEquals(test2.getHealthPoints(), 12);

    }

    public void testGetAttack() {
        CavalryUnit test1 = new CavalryUnit("Test1", 20, 10, 5);
        CavalryUnit test2 = new CavalryUnit("Test2", 15);

        assertEquals(test1.getAttackPoints(), 10);
        assertEquals(test2.getAttackPoints(), 20);
    }

    public void testGetArmor() {
        CavalryUnit test1 = new CavalryUnit("Test1", 20, 10, 5);
        CavalryUnit test2 = new CavalryUnit("Test2", 15);

        assertEquals(test1.getArmorPoints(), 5);
        assertEquals(test2.getArmorPoints(), 12);
    }

    public void testTakeDamage() {
        CavalryUnit test1 = new CavalryUnit("Test1", 15);

        test1.setHealthPoints(2);
        assertEquals(test1.getHealthPoints(), 13);
    }

    public void testToString() {
        CavalryUnit test1 = new CavalryUnit("Test1", 20, 10, 5);

        assertEquals(test1.toString(), "CavalryUnit{name='Test1', health=20, attack=10, armor=5}");
    }
}