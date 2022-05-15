package edu.ntnu.arunang.wargames.gui.factory;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.swing.*;

/**
 * Factory that creates Alert popups.
 */

public class AlertFactory {

    /**
     * Create a warning popup. The popup has a yes button and a cancel button.
     * Used to warn the user about an action.
     *
     * @param message message to be shown on the popup
     * @return an alert element
     */

    public static Alert createWarning(String message) {
        return new Alert(Alert.AlertType.WARNING, message, ButtonType.YES, ButtonType.CANCEL);
    }

    /**
     * Create an error popup. Used to inform the user about an unexpected event.
     *
     * @param message message to be shown on the popup
     * @return an alert element
     */

    public static Alert createError(String message) {
        return new Alert(Alert.AlertType.ERROR, message);
    }

    public static Alert createInformation(String message) {
        return new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
    }

    public static Alert createConfirmation(String message) {
        return new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.CANCEL);
    }
    public static void showJOptionPaneError(String message) {
        JOptionPane.showMessageDialog(null, message, "Hey!", JOptionPane.ERROR_MESSAGE);
    }
}
