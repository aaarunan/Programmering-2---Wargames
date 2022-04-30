package edu.ntnu.arunang.wargames.gui.factory;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Formatter;

import static edu.ntnu.arunang.wargames.gui.factory.TextFactory.createSmallText;

/**
 * Class responsible for creating objects in the GUI.
 * The class is static because it should not be instantiated,
 * since its only role is to create elements, and does not need to make a class.
 * <p>
 * The class can either initialize an element or create it. Methods that
 * creates elements starts with create, and returns a new element. Methods
 * that initializes elements starts with init, and takes in an element as a parameter
 * and return void. These methods can be used to initialize elements from fxml files.
 * <p>
 * The GUIFactory class can also store builders, for easy creation of elements.
 * <p>
 * The reason this class is this abstract and not split up into sub-factories, is because this application is
 * not that big enough to satisfy multiple factories. If necessary some of these methods can be moved to subfactories
 * if needed.
 */

public class ContainerFactory {

    /**
     * Static class should not be instantiated. Constructor is therefor private.
     */

    private ContainerFactory() {
    }


    /**
     * Creates a tableview to show units. Used to show details of an army.
     *
     * @param tableUnits tableview element
     */

    public static void initUnitTable(TableView<Unit> tableUnits) {
        //create the columns and add an observer on them

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

        //Add all the columns to the table

        tableUnits.getColumns().add(type);
        tableUnits.getColumns().add(name);
        tableUnits.getColumns().add(healthPoints);
        tableUnits.getColumns().add(armorPoints);
        tableUnits.getColumns().add(attackPoints);
    }

    /**
     * Create a list card element. Used to make a container with information.
     *
     * @return VBox element
     */

    public static VBox createListCard() {
        VBox vBox = new VBox();
        //add css
        vBox.getStyleClass().add("list-card");

        return vBox;
    }


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

    public static VBox createVBoxElement(int width) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_RIGHT);
        vbox.setPrefWidth(width);
        return vbox;
    }


    /**
     * Crate a gridpane to show stats about an army. Used to show details about an army.
     *
     * @param army the details shown
     * @return gridPane element
     */

    public static GridPane createArmyPane(Army army) {
        GridPane gridPane = new GridPane();

        //add the header texts
        gridPane.add(createSmallText("Count:"), 0, 0, 1, 1);
        gridPane.add(createSmallText("Average HP:"), 0, 1, 1, 1);
        gridPane.add(createSmallText("Average armor:"), 0, 2, 1, 1);
        gridPane.add(createSmallText("Average attack:"), 0, 3, 1, 1);

        Formatter formatter = new Formatter();

        //Add the data
        gridPane.add(createSmallText(Integer.toString(army.size())), 1, 0, 1, 1);
        gridPane.add(createSmallText(String.format("%.2f", army.getAverageHealthPoints())), 1, 1, 1, 1);
        gridPane.add(createSmallText(String.format("%.2f", army.getAverageArmorPoints())), 1, 2, 1, 1);
        gridPane.add(createSmallText(String.format("%.2f", army.getAverageAttackPoints())), 1, 3, 1, 1);

        //add the css
        gridPane.getStyleClass().add("grid-pane");

        return gridPane;
    }

    /**
     * Class that builds a list card element.
     */

    public static class ListCardBuilder {
        private final VBox vBox = createListCard();

        /**
         * Add a text element with a given string
         *
         * @param string text element
         * @return build element
         */

        public ListCardBuilder add(String string) {
            Text text = new Text(string);
            //add css to the element
            text.getStyleClass().add("text-small");
            //add the element to the vbox
            vBox.getChildren().add(text);

            return this;
        }

        /**
         * Finish the building process and return a VBox with the
         * added elements
         *
         * @return VBox element
         */

        public VBox build() {
            return vBox;
        }
    }

    public Pane makeSpacerPane() {
        Pane pane = new Pane();
        pane.setPrefWidth(0);
        pane.setPrefHeight(0);
        return pane;
    }
}

