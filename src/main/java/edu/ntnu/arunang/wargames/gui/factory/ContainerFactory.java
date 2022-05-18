package edu.ntnu.arunang.wargames.gui.factory;

import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Factory for creating various graphical containers.
 * <p>
 * The class can either initialize an element or create it. Methods that creates elements starts with create, and
 * returns a new element. Methods that initializes elements starts with init, and takes in an element as a parameter and
 * return void. These methods can be used to initialize elements from fxml files.
 * <p>
 * The GUIFactory class can also store builders, for easy creation of elements.
 * <p>
 * The reason this class is this abstract and not split up into sub-factories, is because this application is not that
 * big enough to satisfy multiple factories. If necessary some of these methods can be moved to subfactories if needed.
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
     * A spacer pane is used for making a growing gap between other nodes.
     *
     * @return a spacer pane
     */

    public Pane makeSpacerPane() {
        Pane pane = new Pane();
        pane.setPrefWidth(0);
        pane.setPrefHeight(0);
        return pane;
    }

    /**
     * Class that builds a list card element.
     */

    public static class ListCardBuilder {

        private final VBox vBox = createInformationListCard();

        /**
         * Creates a tableview to show units. Used to show details of an army.
         *
         * @param tableUnits tableview element
         */

        @Deprecated
        public static void initTableViewUnits(TableView<Unit> tableUnits) {
            // create the columns and add an observer on them

            TableColumn<Unit, String> type = new TableColumn<>("Type");
            type.setCellValueFactory(unit -> new SimpleStringProperty(unit.getValue().getClass().getSimpleName()));

            TableColumn<Unit, String> name = new TableColumn<>("Name");
            name.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Unit, Integer> healthPoints = new TableColumn<>("Health");
            healthPoints.setCellValueFactory(new PropertyValueFactory<>("healthPoints"));

            TableColumn<Unit, Integer> armorPoints = new TableColumn<>("Armorpoints");
            armorPoints.setCellValueFactory(new PropertyValueFactory<>("armorPoints"));

            TableColumn<Unit, Integer> attackPoints = new TableColumn<>("Attack");
            attackPoints.setCellValueFactory(new PropertyValueFactory<>("attackPoints"));

            // Add all the columns to the table

            tableUnits.getColumns().add(type);
            tableUnits.getColumns().add(name);
            tableUnits.getColumns().add(healthPoints);
            tableUnits.getColumns().add(armorPoints);
            tableUnits.getColumns().add(attackPoints);
        }

        /**
         * Add a text element with a given string
         *
         * @param string text element
         * @return build element
         */

        public ListCardBuilder add(String string) {
            Text text = new Text(string);
            // add css to the element
            text.getStyleClass().add("text-small");
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
