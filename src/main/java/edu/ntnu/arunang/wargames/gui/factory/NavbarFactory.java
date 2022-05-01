package edu.ntnu.arunang.wargames.gui.factory;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/**
 * Factory for creating navigation bar
 */

public class NavbarFactory {

    /**
     * Create a bottom bar. All elements are positioned to the right. User to make a
     * bottom navigation bar
     *
     * @return HBox
     */

    public static HBox createBottomBar() {
        HBox hBox = new HBox();
        //sets the alignment
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPrefHeight(50);
        return hBox;
    }
}
