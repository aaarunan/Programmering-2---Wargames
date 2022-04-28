package edu.ntnu.arunang.wargames.gui;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Formatter;
import java.util.List;

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

public class GUIFactory {

    /**
     * Static class should not be instantiated. Constructor is therefor private.
     */

    private GUIFactory() {
    }

    /**
     * Creates a button that is infinite length. Used for when showing elements in a list.
     *
     * @param text button text
     * @return button element
     */

    public static Button listButton(String text) {
        Button button = new Button(text);
        //Add css
        button.getStyleClass().add("listElement");

        return button;
    }

    /**
     * Create a warning popup. The popup has a yes button and a cancel button.
     * Used to warn the user about an action.
     *
     * @param message message to be shown on the popup
     * @return an alert element
     */

    public static Alert createWarning(String message) {
        return new Alert(Alert.AlertType.WARNING, message, ButtonType.YES, ButtonType.CANCEL);
    }

    /**
     * Create an error popup. Used to inform the user about an unexpected event.
     *
     * @param message message to be shown on the popup
     * @return an alert element
     */

    public static Alert createError(String message) {
        return new Alert(Alert.AlertType.ERROR, message);
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
     * Create a Menu item and add text as text and id.
     *
     * @param text string
     * @return menuItem element
     */

    public static MenuItem createMenuItem(String text) {
        MenuItem menuItem = new MenuItem();
        menuItem.setText(text);
        menuItem.setId(text);

        return menuItem;
    }

    /**
     * Make a textField number only. Adds an
     * Event listener that deletes all regex matches.
     *
     * @param textField text-field that should be number only
     */

    public static void initNumberOnlyTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            //regex values to remove letters
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
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

        return hBox;
    }

    /**
     * Creates a barchart used to represent a battle. Shows the units of the
     * attacking and defending army. Used when simulating a battle.
     *
     * @param attacker attacking army
     * @param defender defending army
     * @return barchart element
     */

    public static LineChart<Number, Number> createBarChart(Army attacker, Army defender) {

        //create the axes of the barchart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Armyname");

        xAxis.setLabel("Army");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Units");
        yAxis.autoRangingProperty().setValue(false);

        int size = defender.size();
        if (attacker.size() > defender.size()) {
           size = attacker.size();
        }
        yAxis.setUpperBound(size+5);

        LineChart<Number, Number> barChart = new LineChart<Number, Number>(xAxis, yAxis);
        barChart.setTitle("Compare");

        //Create the data elements
        XYChart.Series<Number, Number> attackerData = new XYChart.Series<>();
        attackerData.setName(attacker.getName());
        XYChart.Series<Number, Number> defenderData = new XYChart.Series<>();
        defenderData.setName(defender.getName());

        //Add the data to the barchart
        attackerData.getData().add(new XYChart.Data<>(0, attacker.size()));
        defenderData.getData().add(new XYChart.Data<>(0, defender.size()));

        barChart.getData().addAll(attackerData, defenderData);

        barChart.setCreateSymbols(false);
        barChart.setAnimated(false);
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);

        return barChart;
    }

    /**
     * Create a normal default button.
     *
     * @param string text to be shown on the button
     * @return button element
     */

    public static Button createDefaultButton(String string) {
        Button button = new Button(string);
        //set style
        button.getStyleClass().add("button");

        return button;
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

    public static MenuButton createMenuButton(String string) {
        MenuButton menuButton = new MenuButton(string);
        menuButton.getStyleClass().add("menu-button");
        return menuButton;
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
}

