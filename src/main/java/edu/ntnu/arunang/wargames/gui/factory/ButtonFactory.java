package edu.ntnu.arunang.wargames.gui.factory;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

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
        // set style
        button.getStyleClass().add("button");

        return button;
    }

    /**
     * Create a combobox. A combobox is a button with different options
     * that can be selected. The combobox takes in an array of objects.
     * These are the options in the combobox.
     *
     * @param values options of the combobox
     * @param <T>    type of object
     * @return constructed combobox
     */

    public static <T> ComboBox<T> createMenuButton(T[] values) {
        ComboBox<T> comboBox = new ComboBox<>(FXCollections.observableArrayList(values));
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
        // Add css
        button.getStyleClass().add("list-element");

        return button;
    }
}
