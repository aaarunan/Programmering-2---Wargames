package edu.ntnu.arunang.wargames.gui.decorator;

import javafx.scene.text.Text;

public class TextDecorator {
    public static void makeErrorText(Text text) {
        text.setStyle("-fx-fill: red");
    }
}
