package edu.ntnu.arunang.wargames.gui.factory;

import javafx.scene.text.Text;

public class TextFactory {

    /**
     * Create a text element. The element inherits the text-small css class.
     *
     * @param string text to be shown
     * @return text element
     */

    public static Text createSmallText(String string) {
        Text text = new Text(string);
        //add css class
        text.getStyleClass().add("text-small");

        return text;
    }

    /**
     * Create a text element with the title-small css class.
     * Used to make a sub header.
     *
     * @param string text to be shown
     * @return text element
     */

    public static Text createSmallTitle(String string) {
        Text text = new Text(string);
        //add css
        text.getStyleClass().add("title-small");

        return text;
    }

    public static Text createTitle(String string) {
        Text text = new Text(string);
        text.getStyleClass().add("title");
        return text;
    }
}
