package edu.ntnu.arunang.wargames.model.battle;

import edu.ntnu.arunang.wargames.event.EventListener;
import edu.ntnu.arunang.wargames.event.EventType;
import edu.ntnu.arunang.wargames.gui.controller.SimulateCON;
import javafx.application.Platform;

/**
 * Class responsible for listening for attacks in a battle. This class is used for updating the graphical interface for
 * when a simulation is run on a different thread. The class holds an instance of a JavaFX controller (CON) class that
 * is listening for changes.
 */

public class AttackListener implements EventListener {
    private final SimulateCON simulateCON;

    /**
     * Creates an attacklistener with the reference of the object that is listening.
     *
     * @param simulateCON - javafx controller class
     */
    public AttackListener(SimulateCON simulateCON) {
        this.simulateCON = simulateCON;
    }

    /**
     * Updates the gui according to the event type.
     *
     * @param eventType event that is caught
     */

    @Override
    public void update(EventType eventType) {
        switch (eventType) {
            case UPDATE -> updateInterface();
            case FINISH -> finish();
        }
    }

    /**
     * Updates the gui elements according to the controller. Called when event UPDATE has been called.
     */

    private void updateInterface() {
        Platform.runLater(simulateCON::onRepaint);
    }

    /**
     * Finishes the simulation and updates the interface according to the controller. Called when the event FINISH is
     * present.
     */

    private void finish() {
        updateInterface();
        Platform.runLater(simulateCON::onSimulationFinish);
    }
}
