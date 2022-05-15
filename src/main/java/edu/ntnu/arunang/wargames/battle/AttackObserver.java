package edu.ntnu.arunang.wargames.battle;

import edu.ntnu.arunang.wargames.gui.controller.SimulateCON;
import edu.ntnu.arunang.wargames.event.EventType;
import edu.ntnu.arunang.wargames.event.EventListener;
import javafx.application.Platform;

public class AttackObserver implements EventListener {
    private final SimulateCON simulateCON;

    public AttackObserver(SimulateCON simulateCON) {
        this.simulateCON = simulateCON;
    }

    @Override
    public void update(EventType eventtype) {
        switch (eventtype) {
            case UPDATE -> updateArmies();
            case FINISH -> finish();
        }
    }

    private void updateArmies() {
            Platform.runLater(simulateCON::onRepaint);
    }

    private void finish() {
        updateArmies();
        Platform.runLater(simulateCON::onSimulationFinish);
    }
}
