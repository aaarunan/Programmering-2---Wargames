package edu.ntnu.arunang.wargames.gui.factory;

import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

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

    public static MenuButton createMenuButton(String string) {
        MenuButton menuButton = new MenuButton(string);
        menuButton.getStyleClass().add("menu-button");
        return menuButton;
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
