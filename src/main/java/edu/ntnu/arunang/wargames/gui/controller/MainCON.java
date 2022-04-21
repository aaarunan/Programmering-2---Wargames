package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.gui.ArmySingleton;
import edu.ntnu.arunang.wargames.gui.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the mainpage
 */

public class MainCON {

    private final ArmySingleton armySingleton = ArmySingleton.getInstance();

    /**
     * Redirect to the listarmy page and set simulate to false in the singleton.
     *
     * @param event triggering event.
     */

    @FXML
    void onBtnArmies(ActionEvent event) {
        armySingleton.setSimulate(false);
        GUI.setSceneFromActionEvent(event, "listArmy");
    }

    /**
     * Quits the application.
     */

    @FXML
    void onBtnQuit() {
        Platform.exit();
    }

    /**
     * Redirects to the listArmy and sets simulation to true.
     *
     * @param event triggering event
     */

    @FXML
    void onBtnSimulate(ActionEvent event) {
        /*
        armySingleton.setSimulate(true);
        GUI.setSceneFromActionEvent(event, "listArmy");
        */
        armySingleton.setSimulate(true);
        armySingleton.setAttacker(armySingleton.getArmies().get(0));
        armySingleton.setDefender(armySingleton.getArmies().get(0));
        GUI.setSceneFromActionEvent(event, "simulate");
    }

    /**
     * Clears the singleton when the page is initialized. This is to flush out
     * information from past activities.
     */

    @FXML
    void initialize() {
        ArmySingleton.getInstance().clear();
    }
}
