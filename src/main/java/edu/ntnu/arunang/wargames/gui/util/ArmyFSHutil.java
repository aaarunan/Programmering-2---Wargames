package edu.ntnu.arunang.wargames.gui.util;

import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.fsh.FileFormatException;
import edu.ntnu.arunang.wargames.gui.factory.AlertFactory;
import edu.ntnu.arunang.wargames.model.army.Army;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * ArmyFSH util class makes ArmyFSH methods abstract with gui. This class reduces code repetition
 * that can occur using the standalone ArmyFSH.
 */

public class ArmyFSHutil {

    /**
     * Write an army. This method has override protection, and prompts the user to confirm overriding of armies.
     *
     * @param army army that is being written
     * @return true if the army was written to disk or false
     */

    public static boolean writeArmy(Army army) {
        ArmyFSH armyFSH = new ArmyFSH();
        // checks if file exists
        if (armyFSH.fileExists(new File(ArmyFSH.getPath(army.getName())))) {
            Alert alert = AlertFactory.createConfirmation(
                    String.format("Army '%s' already exists, do you want to override it?", army.getName()));
            Optional<ButtonType> result = alert.showAndWait();

            // cancels the process if the user declines
            if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
                return false;
            }
        }
        // Checks if the army can be written
        try {
            armyFSH.writeArmy(army);
        } catch (IOException e) {
            AlertFactory.createError("Could not overwrite file. File might be in use... \n " + e.getMessage()).show();
            return false;
        } catch (Exception e) {
            AlertFactory.createError("Un unexpected exception occurred... \n " + e.getMessage()).show();
            return false;
        }
        return true;
    }

    /**
     * Loads the army from a file. If loading fails, it will print out alert boxes.
     *
     * @param file file that is being loaded
     * @return army, or null if unsuccessful
     */

    public static Army loadArmyFromFile(File file) {
        ArmyFSH armyFSH = new ArmyFSH();
        Army army = null;

        try {
            army = armyFSH.loadFromFile(file);
        } catch (IOException e) {
            AlertFactory.createError("Army could not be loaded...\n" + e.getMessage()).show();
        } catch (FileFormatException e) {
            AlertFactory.createError("Army is wrongly formatted! \n" + e.getMessage()).show();
        }
        return army;
    }
}
