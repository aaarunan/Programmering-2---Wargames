package edu.ntnu.arunang.wargames;

import edu.ntnu.arunang.wargames.gui.ArmySingleton;
import edu.ntnu.arunang.wargames.gui.GUI;
import javafx.application.Application;

/**
 * Main class of the application. Loads all the necessary resources and starts the gui.
 */

public class Main {

    /**
     * Main method launches the gui and loads the armies.
     *
     * @param args args of the system
     */

    public static void main(String[] args) {
        ArmySingleton.getInstance();
        Application.launch(GUI.class, args);
    }

}
