package edu.ntnu.arunang.wargames;

import edu.ntnu.arunang.wargames.fsh.FileFormatException;
import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.gui.ArmySingleton;
import edu.ntnu.arunang.wargames.gui.GUI;
import javafx.application.Application;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

/**
 * Main class of the application. Loads all the necessary resources and starts the gui.
 */

public class Main {

    /**
     * Main method launches the gui and loads the armies.
     * @param args args of the system
     */

    public static void main(String[] args) {
        loadArmies();
        Application.launch(GUI.class, args);
    }

    /**
     * Loads the armies from /src/main/resources/army
     */

    public static void loadArmies() {
        //get all the files
        File folder = new File(FileSystems.getDefault().getPath("src", "main", "resources", "army").toString());
        File[] files = folder.listFiles();

        //cancel process if there are no files
        if (files == null || files.length == 0) {
            return;
        }

        ArmyFSH armyFSH = new ArmyFSH();
        ArrayList<Army> armies = new ArrayList<>();

        //read all the files and inform the user if the files are formatted wrong or something unexpected happened
        for (File file : files) {
            try {
                armies.add(armyFSH.loadFromFile(file));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Some files Could not be read \n" + e.getMessage(),
                        "Hey!", JOptionPane.ERROR_MESSAGE);
            } catch (FileFormatException e) {
                System.out.println("1");
                JOptionPane.showMessageDialog(null, "Some files are wrongly formatted.. \n" + e.getMessage(),
                        "Hey!", JOptionPane.WARNING_MESSAGE);
            }
        }
        //cache the armies in the singleton
        ArmySingleton.getInstance().setArmies(armies);
    }
}
