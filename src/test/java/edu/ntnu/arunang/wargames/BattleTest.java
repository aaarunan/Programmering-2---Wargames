package edu.ntnu.arunang.wargames;

import edu.ntnu.arunang.wargames.Unit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    CavalryUnit opUnit = new CavalryUnit("opUnit", 10000);
    CommanderUnit comUnit = new CommanderUnit("comUnit", 40);
    RangedUnit ranUnit = new RangedUnit("ranUnit", 50);
    InfantryUnit infUnit = new InfantryUnit("infUnit", 60);

    Army attacker = new Army("Attacker");
    Army defender = new Army("Defender");

    Battle battle = new Battle(attacker, defender);

    @Test
    @DisplayName("Test empty on when both armies are empty")
    void testBattleOnFullEmpty() {
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> battle.simulate(),
                "All armies must have atleast one unit."

        );

        assertTrue(thrown.getMessage().contains("All armies must have atleast one unit."));
    }

    @Test
    @DisplayName("Test simulation on one empty army ")
    void testBattleOnOneEmptyArmy() {
        attacker.add(infUnit);
        battle = new Battle(attacker, defender);
        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> battle.simulate(),
                "All armies must have atleast one unit."

        );

        assertTrue(thrown.getMessage().contains("All armies must have atleast one unit."));
    }

    @Test
    @DisplayName("Simulates an unfavorable match for the defender.")
    void testWinBattle() {

        attacker.add(opUnit);
        defender.add(comUnit);
        battle = new Battle(attacker, defender);

        assertEquals(attacker, battle.simulate());
    }

    @Test
    @DisplayName("Tests simulation on two similar armies")
    void testSimilarArmies() {
        int count = 1000;

        attacker.add(infUnit, count);
        defender.add(infUnit, count);

        battle = new Battle(attacker, defender);

        assertEquals(attacker, battle.simulate());
    }
}