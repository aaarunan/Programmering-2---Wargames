package edu.ntnu.arunang.wargames.gui.factory;

import javafx.scene.text.Text;

/**
 * Factory that creates text elements. Text elements created can be editable.
 */

public class TextFactory {

    /**
     * Create a text element. The element inherits the text-small css class.
     *
     * @param string text to be shown
     * @return text element
     */

    public static Text createSmallText(String string) {
        Text text = new Text(string);
        // add css class
        text.getStyleClass().add("text-small");

        return text;
    }

    /**
     * Create a text element with the title-small css class. Used to make a sub header.
     *
     * @param string text to be shown
     * @return text element
     */

    public static Text createSmallTitle(String string) {
        Text text = new Text(string);
        // add css
        text.getStyleClass().add("title-small");

        return text;
    }

    /**
     * Creates a title text element. The text elements inherts the 'title' css class.
     *
     * @param string text shown
     * @return constructed text element.
     */

    public static Text createTitle(String string) {
        Text text = new Text(string);
        text.getStyleClass().add("title");
        return text;
    }
}
