package edu.ntnu.arunang.wargames.observer;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Battle;
import edu.ntnu.arunang.wargames.gui.controller.SimulateCON;

public class HitObserver implements Observer {
    private final Battle battle;
    private final Army attacker;
    private final Army defender;

    private final SimulateCON simulateCON;
    private int i;

    public HitObserver(Army attacker, Army defender, Battle battle, SimulateCON simulateCON) {
        this.battle = battle;
        this.simulateCON = simulateCON;
        this.attacker = attacker;
        this.defender = defender;
    }


    @Override
    public void update() {
        simulateCON.updateBarChart(0);
        System.out.println("hit");
    }
}
