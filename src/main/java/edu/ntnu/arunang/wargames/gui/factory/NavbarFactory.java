package edu.ntnu.arunang.wargames.gui.factory;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * Factory for creating navigation bar
 */

public class NavbarFactory {

    /**
     * Create a bottom bar. All elements are positioned to the right. Used to make a bottom navigation bar
     *
     * @return HBox
     */

    public static HBox createBottomBar(Node... nodes) {
        HBox buttonBar = new HBox();
        // sets the alignment
        buttonBar.setPrefHeight(50);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        buttonBar.getChildren().addAll(nodes);

        return buttonBar;
    }
}
