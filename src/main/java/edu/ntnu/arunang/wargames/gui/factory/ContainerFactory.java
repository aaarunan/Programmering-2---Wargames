package edu.ntnu.arunang.wargames.gui.factory;

import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Factory for creating various graphical containers.
 */

public class ContainerFactory {

    /**
     * Static class should not be instantiated. Constructor is therefor private.
     */

    private ContainerFactory() {
    }

    /**
     * Create a list card element. Used to make a container with information.
     *
     * @return VBox element
     */

    public static VBox createInformationListCard() {
        VBox vBox = new VBox();
        // add css
        vBox.getStyleClass().add("list-card");

        return vBox;
    }

    /**
     * Creates a VBox with alignment center. If the width is lower than 0, the width will be set to fit width.
     *
     * @param width preferred with of the element
     * @return a VBox with position centered
     */

    public static VBox createCenteredVBox(int width) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_RIGHT);
        if (width > 0) {
            vbox.setPrefWidth(width);
        } else {
            vbox.fillWidthProperty().setValue(true);
        }

        return vbox;
    }

    /**
     * Class that builds a list card element.
     */

    public static class ListCardBuilder {

        private final VBox vBox = createInformationListCard();

        /**
         * Add a text element with a given string
         *
         * @param string text element
         * @return build element
         */

        public ListCardBuilder add(String string) {
            Label text = TextFactory.createSmallText(string);
            // add css to the element
            TextDecorator.makeColored(text);
            // add the element to the vbox
            vBox.getChildren().add(text);

            return this;
        }

        /**
         * Finish the building process and return a VBox with the added elements
         *
         * @return VBox element
         */

        public VBox build() {
            return vBox;
        }
    }
}
