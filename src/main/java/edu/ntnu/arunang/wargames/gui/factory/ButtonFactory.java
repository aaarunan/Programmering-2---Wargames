package edu.ntnu.arunang.wargames.gui.factory;

import edu.ntnu.arunang.wargames.Terrain;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Factory for creating clickable elements.
 */

public class ButtonFactory {
    /**
     * Create a normal default button.
     *
     * @param string text to be shown on the button
     * @return button element
     */

    public static Button createDefaultButton(String string) {
        Button button = new Button(string);
        //set style
        button.getStyleClass().add("button");

        return button;
    }

    public static <T> ComboBox<T> createMenuButton(T[] values) {
        ComboBox<T> comboBox = new ComboBox(FXCollections.observableArrayList(values));
        comboBox.getStyleClass().add("menu-button");
        return comboBox;
    }

    /**
     * Creates a button that is infinite length. Used for when showing elements in a list.
     *
     * @param text button text
     * @return button element
     */

    public static Button listButton(String text) {
        Button button = new Button(text);
        //Add css
        button.getStyleClass().add("list-element");

        return button;
    }

    /**
     * Make a textField number only. Adds an
     * Event listener that deletes all regex matches.
     *
     * @param textField text-field that should be number only
     */

    public static void initNumberOnlyTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            //regex values to remove letters
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Create a Menu item and add text as text and id.
     *
     * @param text string
     * @return menuItem element
     */

    public static MenuItem createMenuItem(String text) {
        MenuItem menuItem = new MenuItem();
        menuItem.setText(text);
        menuItem.setId(text);

        return menuItem;
    }

}
