package edu.ntnu.arunang.wargames.battle;

import edu.ntnu.arunang.wargames.model.army.Army;
import edu.ntnu.arunang.wargames.model.battle.Battle;
import edu.ntnu.arunang.wargames.model.unit.CavalryUnit;
import edu.ntnu.arunang.wargames.model.unit.CommanderUnit;
import edu.ntnu.arunang.wargames.model.unit.InfantryUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BattleTest {

    CavalryUnit opUnit = new CavalryUnit("opUnit", 10000);
    CommanderUnit comUnit = new CommanderUnit("comUnit", 40);
    InfantryUnit infUnit = new InfantryUnit("infUnit", 60);


    @Test
    @DisplayName("Test when both armies are empty")
    void testBattleOnFullEmpty() {
        Army attacker = new Army("Attacker");
        Army defender = new Army("Defender");
        Battle battle = new Battle(attacker, defender, null);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> battle.simulate(0),
                "All armies must have at least one unit."

        );

        assertEquals("All armies must have at least one unit.", thrown.getMessage());
    }

    @Test
    @DisplayName("Test simulation on one empty army ")
    void testBattleOnOneEmptyArmy() {
        Army attacker = new Army("Attacker");
        Army defender = new Army("Defender");
        Battle battle = new Battle(attacker, defender, null);

        attacker.add(infUnit);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> battle.simulate(0),
                "All armies must have at least one unit."

        );

        assertEquals("All armies must have at least one unit.", thrown.getMessage());
    }

    @Test
    @DisplayName("Simulates an unfavorable match for the defender.")
    void testWinBattle() {
        Army attacker = new Army("Attacker");
        Army defender = new Army("Defender");
        Battle battle = new Battle(attacker, defender, null);

        attacker.add(opUnit);
        defender.add(comUnit);

        assertEquals(attacker, battle.simulate(0));
    }

    @Test
    @DisplayName("Tests simulation on two similar armies")
    void testSimilarArmies() {
        Army attacker = new Army("Attacker");
        Army defender = new Army("Defender");
        Battle battle = new Battle(attacker, defender, null);

        int count = 1000;

        attacker.add(infUnit, count);
        defender.add(infUnit, count);

        assertEquals(attacker, battle.simulate(0));
    }

    @Test
    @DisplayName("Test on negative delay on simulation")
    void testNegativeDelay() {
        Army attacker = new Army("Attacker");
        Army defender = new Army("Defender");
        Battle battle = new Battle(attacker, defender, null);

        attacker.add(opUnit);
        defender.add(opUnit);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> battle.simulate(-1),
                "Delay must be positive"

        );

        assertEquals("Delay must be positive", thrown.getMessage());
    }
}