package edu.ntnu.arunang.wargames.gui.decorator;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class TextDecorator {

    /**
     * Adds an icon to a label.
     * The label has to be defined through a css class located in the icon-text.css file named
     * %icon%-icon.
     *
     * @param label The label element to decorate.
     * @param icon  The icon to add to this label.
     */
    public static void setIcon(Label label, String icon) {

        label.getStyleClass().add("icon-text");
        label.getStyleClass().add(String.format("%s-icon", icon));
    }

    /**
     * Removes all icons from a label with an icon.
     *
     * @param label The label to remove the icon from.
     */
    public static void removeIcon(Text label) {
        label.getStyleClass().remove("icon-label");
        label.getStyleClass().removeIf(styleClass -> styleClass.endsWith("-icon"));
    }

    /**
     * Decorate text mean for showing errors
     * @param text text that is being styled
     */
    public static void makeErrorText(Node text) {
        text.getStyleClass().add("text-error");
    }

    /**
     * Decorate text with color from text-colored css class.
     * @param text text that is being styled
     */
    public static void makeColored(Node text) {
        text.getStyleClass().add("text-colored");
    }
}
