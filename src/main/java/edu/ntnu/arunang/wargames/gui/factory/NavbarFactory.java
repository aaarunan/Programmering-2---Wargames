package edu.ntnu.arunang.wargames.gui.factory;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
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

    public static ButtonBar createBottomBar() {
        ButtonBar buttonBar = new ButtonBar();
        //sets the alignment
        buttonBar.setPrefHeight(50);
        return buttonBar;
    }
}
