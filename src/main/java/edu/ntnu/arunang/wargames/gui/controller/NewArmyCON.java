package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.container.UnitContainerManager;
import edu.ntnu.arunang.wargames.gui.factory.AlertFactory;
import edu.ntnu.arunang.wargames.gui.factory.ButtonFactory;
import edu.ntnu.arunang.wargames.gui.factory.ContainerFactory;
import edu.ntnu.arunang.wargames.gui.factory.NavbarFactory;
import edu.ntnu.arunang.wargames.model.Army;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the newArmy page.
 */

public class NewArmyCON {
    private final Army army = new Army("Army name");
    private final ArmyContainer armyGridPane = new ArmyContainer(army);
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
        ArmyFSH armyFSH = new ArmyFSH();

        txtErrorMsg.setText("");
        if (txtArmyName.getText().isBlank()) {
            txtErrorMsg.setText("Choose an army name");
            return;
        }

        // checks if file exists
        if (armyFSH.fileExists(new File(ArmyFSH.getPath(army.getName())))) {
            Alert alert = AlertFactory.createConfirmation(
                    String.format("Army '%s' already exists, do you want to override it?", army.getName()));
            Optional<ButtonType> result = alert.showAndWait();

            // cancels the process if the user declines
            if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
                return;
            }
        }
        // Checks if the army can be written
        try {
            armyFSH.writeArmy(army);
        } catch (IOException e) {
            AlertFactory.createError("Could not overwrite file. File might be in use... \n " + e.getMessage()).show();
            return;
        } catch (Exception e) {
            AlertFactory.createError("Un unexpected exception occurred... \n " + e.getMessage()).show();
            return;
        }

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
