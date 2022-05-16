package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.container.UnitInformationContainer;
import edu.ntnu.arunang.wargames.gui.factory.AlertFactory;
import edu.ntnu.arunang.wargames.gui.factory.ButtonFactory;
import edu.ntnu.arunang.wargames.gui.factory.ContainerFactory;
import edu.ntnu.arunang.wargames.gui.factory.NavbarFactory;
import edu.ntnu.arunang.wargames.unit.Unit;
import edu.ntnu.arunang.wargames.unit.UnitFactory;
import edu.ntnu.arunang.wargames.unit.UnitType;
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
    private VBox vBoxAdd;
    @FXML
    private Text txtErrorMsg;
    @FXML
    private VBox armyContainer;
    @FXML
    private BorderPane borderPane;

    private final Army army = new Army("Army name");

    private UnitType unitType;

    private ArmyContainer armyGridPane = new ArmyContainer(army);

    /**
     * Changes the name of the army when typed in the army name textfield, and updates
     * the detail container accordingly.
     */

    @FXML
    void onArmyName() {
        txtErrorMsg.setText("");
        //throw error if name is empty
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
     * Saves the army to the /resources/army folder.
     * If the file already exists, the user will be warned.
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

        //checks if file exists
        if (armyFSH.fileExists(new File(ArmyFSH.getPath(army.getName())))) {
            Alert alert = AlertFactory.createConfirmation(String.format("Army '%s' already exists, do you want to override it?", army.getName()));
            Optional<ButtonType> result = alert.showAndWait();

            //cancels the process if the user declines
            if (result.get() == ButtonType.CANCEL) {
                return;
            }
        }
        //Checks if the army can be written
        try {
            armyFSH.writeArmy(army);
        } catch (IOException e) {
            AlertFactory.createError("Could not overwrite file. File might be in use... \n " + e.getMessage()).show();
        } catch (Exception e) {
            AlertFactory.createError("Un unexpected exception occurred... \n " + e.getMessage()).show();
            return;
        }

        //removes the army from the singleton if successful, and adds the new army
        GUI.setSceneFromActionEvent(event, "listArmy");
    }

    /**
     * Add units to an army. This also updates the gui accordingly.
     */

    @FXML
    void onAdd() {
        txtErrorMsg.setText("");
        ArrayList<Unit> units = new ArrayList<>();

        //check if the user has choosen a unittype
        if (unitType == null) {
            txtErrorMsg.setText("Choose an Unittype.");
            return;
        }

        //try to create a unit from the user input
        try {
            units = (ArrayList<Unit>) UnitFactory.constructUnitsFromString(unitType.toString(), fieldName.getText(), Integer.parseInt(fieldHealthPoints.getText()), Integer.parseInt(fieldCount.getText()));
            army.add(units);
        } catch (IllegalArgumentException e) {
            txtErrorMsg.setText(e.getMessage());
        } catch (Exception e) {
            AlertFactory.createError("Unexpected error occured: \n" + e.getMessage()).show();
        }

        repaintUnits();

        Button btnDelete = new Button("Delete");

        Unit target = units.get(0);

        //Add a card to the history container
        ContainerFactory.ListCardBuilder listCardBuilder = new ContainerFactory.ListCardBuilder();
        VBox vBox = listCardBuilder.add("Type: " + target.getClass().getSimpleName()).add("Name: " + target.getName()).add("Health points: " + target.getHealthPoints()).add("Count: " + units.size()).build();

        ArrayList<Unit> finalUnits = units;
        btnDelete.setOnAction(event -> onDeleteUnits(event, finalUnits));

        //add all the elements and update the page
        vBox.getChildren().add(btnDelete);
        vBoxAdd.getChildren().add(vBox);
        repaintDetails();
    }

    void onDeleteUnits(ActionEvent event, List<Unit> units) {
        for (Unit unit : units) {
            army.remove(unit);
            repaintDetails();
            repaintUnits();
        }
    }

    /**
     * Initializes the page. Initializes number only fields and the tableview.
     */

    @FXML
    void initialize() {
        initMenuUnitType();
        armyContainer.getChildren().add(armyGridPane.getGridPane());
        ButtonFactory.initNumberOnlyTextField(fieldCount);
        ButtonFactory.initNumberOnlyTextField(fieldHealthPoints);
        initBottomBar();
    }

    /**
     * Update the stats of the army.
     */

    void repaintDetails() {
        armyGridPane.updateData();
    }

    private void repaintUnits() {
        unitsWindow.getChildren().clear();
        unitsWindow.getChildren().add(new UnitInformationContainer(army, false).getFlowpane());
    }

    /**
     * Intializes the menuButton for unitType. Loops through the UnitType enum.
     */

    void initMenuUnitType() {
        for (UnitType type : UnitType.values()) {
            menuUnitType.getItems().add(type);
        }
        menuUnitType.setOnAction(event -> this.unitType = menuUnitType.getValue());
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
}
