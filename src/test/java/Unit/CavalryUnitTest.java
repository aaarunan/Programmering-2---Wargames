package Unit;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;


public class CavalryUnitTest extends TestCase {

    CavalryUnit attacker = new CavalryUnit("Test1", 20);
    CavalryUnit defender = new CavalryUnit("Test2", 15);

    @Test
    @DisplayName("Attacking deals x damage")
    public void testAttack() {
        attacker.attack(defender);
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(2, defender.getHealthPoints());

        //player should be dead

        attacker.attack(defender);
        assertEquals(20, attacker.getHealthPoints());
        assertEquals(-11, defender.getHealthPoints());

    }

    @Test
    @DisplayName("Playes is dead when HP is less than or equal to 0")
    public void testisDead() {
        testAttack();
        assertEquals(defender.isDead(), true);
    }



}

