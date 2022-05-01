package edu.ntnu.arunang.wargames.gui.decorator;

import javafx.scene.control.Button;


public class ButtonDecorator {
    private ButtonDecorator() {

    }

    public static void makeListElementActive(Button button) {
        button.getStyleClass().clear();
        button.getStyleClass().add("button");
        button.getStyleClass().add("list-element");
        button.getStyleClass().add("list-element-active");
    }

    public static void makeListElementHighlighted(Button button) {
        button.getStyleClass().clear();
        button.getStyleClass().add("button");
        button.getStyleClass().add("list-element");
        button.getStyleClass().add("list-element-highlighted");
    }

    public static void makeListElementDefault(Button button) {
        button.getStyleClass().clear();
        button.getStyleClass().add("button");
        button.getStyleClass().add("list-element");
    }

    public static void makeDeleteButton(Button button) {
        button.getStyleClass().clear();
        button.getStyleClass().add("button");
        button.getStyleClass().add("button-delete");
    }
}
