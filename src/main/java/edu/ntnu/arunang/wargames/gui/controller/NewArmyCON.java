package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.StateHandler;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.container.UnitContainerManager;
import edu.ntnu.arunang.wargames.gui.factory.AlertFactory;
import edu.ntnu.arunang.wargames.gui.factory.ButtonFactory;
import edu.ntnu.arunang.wargames.gui.factory.ContainerFactory;
import edu.ntnu.arunang.wargames.gui.factory.NavbarFactory;
import edu.ntnu.arunang.wargames.gui.util.ArmyFSHutil;
import edu.ntnu.arunang.wargames.model.Army;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.model.unit.Unit;
import edu.ntnu.arunang.wargames.model.unit.UnitFactory;
import edu.ntnu.arunang.wargames.model.unit.UnitType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the newArmy page.
 */

public class NewArmyCON {
    @FXML
    private TextField fieldArmyName;
    @FXML
    private VBox unitsWindow;
    @FXML
    private Text txtArmyName;
    @FXML
    private TextField fieldHealthPoints;
    @FXML
    private TextField fieldCount;
    @FXML
    private TextField fieldName;
    @FXML
    private ComboBox<UnitType> menuUnitType;
    @FXML
    private VBox historyContainer;
    @FXML
    private Text txtErrorMsg;
    @FXML
    private VBox armyContainer;
    @FXML

    private BorderPane borderPane;
    private UnitType unitType;

    private final Army army = new Army("Army name");
    private final ArmyContainer armyGridPane = new ArmyContainer(army);

    /**
     * Changes the name of the army when typed in the army name text-field, and updates the detail container accordingly.
     */

    @FXML
    void onArmyName() {
        txtErrorMsg.setText("");
        // throw error if name is empty
        try {
            army.setName(fieldArmyName.getText());
        } catch (IllegalArgumentException e) {
            txtErrorMsg.setText(e.getMessage());
        }
        txtArmyName.setText(army.getName());
    }

    /**
     * Cancel creation of new army. And redirect to the listArmy page.
     *
     * @param event triggering event from button
     */

    void onCancel(ActionEvent event) {
        GUI.setSceneFromActionEvent(event, "listArmy");
    }

    /**
     * Saves the army to the /resources/army folder. If the file already exists, the user will be warned.
     *
     * @param event triggering event.
     */

    void onSave(ActionEvent event) {

        txtErrorMsg.setText("");
        if (txtArmyName.getText().isBlank()) {
            txtErrorMsg.setText("Specify an army name");
            return;
        }


        if(!ArmyFSHutil.writeArmy(army)) {
            return;
        }

        StateHandler.getInstance().setSimulate(false);

        // removes the army from the singleton if successful, and adds the new army
        GUI.setSceneFromActionEvent(event, "listArmy");
    }

    /**
     * Add units to an army. This also updates the gui accordingly.
     */

    @FXML
    void onAdd() {
        txtErrorMsg.setText("");
        ArrayList<Unit> units;

        // check if the user has choosen a unittype
        if (unitType == null) {
            txtErrorMsg.setText("Choose an Unittype.");
            return;
        }

        // try to create a unit from the user input
        try {
            units = (ArrayList<Unit>) UnitFactory.constructUnitsFromString(unitType.toString(), fieldName.getText(),
                    Integer.parseInt(fieldHealthPoints.getText()), Integer.parseInt(fieldCount.getText()));
            army.add(units);
        } catch (IllegalArgumentException e) {
            txtErrorMsg.setText(e.getMessage());
            return;
        } catch (Exception e) {
            AlertFactory.createError("Unexpected error occured: \n" + e.getMessage()).show();
            return;
        }

        repaintUnits();


        Unit target = units.get(0);
        // Add a card to the history container
        ContainerFactory.ListCardBuilder listCardBuilder = new ContainerFactory.ListCardBuilder();
        VBox listCard = listCardBuilder.add("Type: " + target.getClass().getSimpleName()).add("Name: " + target.getName())
                .add("Health points: " + target.getHealthPoints()).add("Count: " + units.size()).build();

        Button btnDelete = new Button("Delete");
        ArrayList<Unit> finalUnits = units;
        btnDelete.setOnAction(event -> onDeleteUnits(finalUnits, listCard));

        // add all the elements and update the page
        listCard.getChildren().add(btnDelete);
        historyContainer.getChildren().add(listCard);
        repaintDetails();
    }

    /**
     * Deletes a list of units from the army.
     *
     * @param units units that are removed
     */

    void onDeleteUnits(List<Unit> units, VBox vBox) {
        for (Unit unit : units) {
            army.remove(unit);
        }
        repaintDetails();
        repaintUnits();

        historyContainer.getChildren().remove(vBox);
    }

    /**
     * Update the stats of the army.
     */

    void repaintDetails() {
        armyGridPane.updateData();
    }

    /**
     * Repaint the unit cards.
     */

    private void repaintUnits() {
        unitsWindow.getChildren().clear();
        unitsWindow.getChildren().add(new UnitContainerManager(army, false).getFlowpane());
    }

    /**
     * Initializes the bottombar.
     */

    void initBottomBar() {
        HBox bottomBar = NavbarFactory.createBottomBar();
        Button back = ButtonFactory.createDefaultButton("Cancel");
        back.setOnAction(this::onCancel);
        Button save = ButtonFactory.createDefaultButton("Save");
        save.setOnAction(this::onSave);
        bottomBar.getChildren().addAll(back, save);
        borderPane.setBottom(bottomBar);
    }

    /**
     * Initializes the page. Initializes number only fields, and bottombar.
     */

    @FXML
    void initialize() {
        for (UnitType type : UnitType.values()) {
            menuUnitType.getItems().add(type);
        }
        menuUnitType.setOnAction(event -> this.unitType = menuUnitType.getValue());
        armyContainer.getChildren().add(armyGridPane.getGridPane());
        TextDecorator.makeNumberFieldOnly(fieldCount);
        TextDecorator.makeNumberFieldOnly(fieldHealthPoints);
        TextDecorator.makeErrorText(txtErrorMsg);

        initBottomBar();

    }
}
