package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.gui.StateHandler;
import edu.ntnu.arunang.wargames.gui.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


/**
 * Controller for the mainpage.
 */

public class MainCON {

    /**
     * Redirect to the list- army page and set simulate to false in the singleton.
     *
     * @param event triggering event.
     */

    @FXML
    void onArmies(ActionEvent event) {
        StateHandler.getInstance().setSimulate(false);
        GUI.setSceneFromActionEvent(event, "listArmy");
    }

    /**
     * Redirects to the listArmy and sets simulation to true.
     *
     * @param event triggering event
     */

    @FXML
    void onSimulate(ActionEvent event) {
        StateHandler.getInstance().setSimulate(true);
        GUI.setSceneFromActionEvent(event, "listArmy");
    }

    /**
     * Quits the application.
     */

    @FXML
    void onCreate(ActionEvent event) {
        GUI.setSceneFromActionEvent(event, "newArmy");
    }


}
