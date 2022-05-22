package edu.ntnu.arunang.wargames;

import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.factory.AlertFactory;
import javafx.application.Application;

import java.io.File;

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
        //make army directory if it does not exist already
        File file = new File(ArmyFSH.getDir());
        if (!file.mkdirs()) {
            if (!file.exists()) {
                AlertFactory.showJOptionPaneError("Could not make an army directory! \n" + file.getAbsolutePath());
            }
        }

        //Start the gui
        Application.launch(GUI.class, args);
    }



}
