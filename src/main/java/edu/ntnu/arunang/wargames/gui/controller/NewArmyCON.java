package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.gui.ArmySingleton;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.GUIFactory;
import edu.ntnu.arunang.wargames.unit.Unit;
import edu.ntnu.arunang.wargames.unit.UnitFactory;
import edu.ntnu.arunang.wargames.unit.UnitType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller for the newArmy page.
 */

public class NewArmyCON {

    private final Army army = new Army("Army name");
    private final ArmySingleton armySingleton = ArmySingleton.getInstance();
    @FXML
    private TextField fieldArmyName;
    @FXML
    private TableView<Unit> tableUnits;
    @FXML
    private Text txtArmyName;
    @FXML
    private TextField fieldHealthPoints;
    @FXML
    private TextField fieldCount;
    @FXML
    private TextField fieldName;
    @FXML
    private MenuButton menuUnitType;
    @FXML
    private VBox vBoxAdd;
    @FXML
    private Text txtErrorMsg;
    @FXML
    private HBox armyContainer;

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

    @FXML
    void onCancel(ActionEvent event) {
        GUI.setSceneFromActionEvent(event, "listArmy");
    }

    /**
     * Saves the army to the /resources/army folder.
     * If the file already exists, the user will be warned.
     *
     * @param event triggering event.
     */

    @FXML
    void onSave(ActionEvent event) {
        ArmyFSH armyFSH = new ArmyFSH();

        //checks if file exists
        if (armyFSH.fileExists(new File(ArmyFSH.getPath(army.getName())))) {
            Alert alert = GUIFactory.createWarning(String.format("Army '%s' already exists, do you want to override it?", army.getName()));
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
            GUIFactory.createError("Could not overwrite file. File might be in use... \n " + e.getMessage()).show();
        } catch (Exception e) {
            GUIFactory.createError("Un unexpected exception occurred... \n " + e.getMessage()).show();
            return;
        }

        //removes the army from the singleton if successful, and adds the new army
        armySingleton.removeArmy(army);
        armySingleton.addArmy(army);
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
        if (menuUnitType.getText().equals("UnitType")) {
            txtErrorMsg.setText("Choose an Unittype.");
            return;
        }

        //try to create a unit from the user input
        try {
            units = (ArrayList<Unit>) UnitFactory.constructUnitsFromString(menuUnitType.getId(), fieldName.getText(), Integer.parseInt(fieldHealthPoints.getText()), Integer.parseInt(fieldCount.getText()));
            army.add(units);
            units.forEach(unit -> tableUnits.getItems().add(unit));
        } catch (IllegalArgumentException e) {
            txtErrorMsg.setText(e.getMessage());
        } catch (Exception e) {
            GUIFactory.createError("Unexpected error occured: \n" + e.getMessage()).show();
        }

        Button btnDelete = new Button("Delete");

        ArrayList<Unit> finalUnits = units;
        Unit target = units.get(0);

        //Add a card to the history container
        GUIFactory.ListCardBuilder listCardBuilder = new GUIFactory.ListCardBuilder();
        VBox vBox = listCardBuilder.add("Type: " + target.getClass().getSimpleName()).add("Name: " + target.getName()).add("Health points: " + target.getHealthPoints()).add("Count: " + units.size()).build();

        btnDelete.setOnAction(event -> {
            for (Unit unit : finalUnits) {
                army.remove(unit);
                vBoxAdd.getChildren().remove(vBox);
                tableUnits.getItems().remove(unit);
                updateDetails();
            }
        });

        //add all the elements and update the page
        vBox.getChildren().add(btnDelete);
        vBoxAdd.getChildren().add(vBox);
        updateDetails();
    }

    /**
     * Initializes the page. Initializes number only fields and the tableview.
     */

    @FXML
    void initialize() {
        initMenuUnitType();
        GUIFactory.initNumberOnlyTextField(fieldCount);
        GUIFactory.initNumberOnlyTextField(fieldHealthPoints);
        GUIFactory.initUnitTable(tableUnits);
        updateDetails();
    }

    /**
     * Update the stats of the army, and the army name when
     */

    void updateDetails() {
        armyContainer.getChildren().clear();
        armyContainer.getChildren().add(GUIFactory.createArmyPane(army));
    }


    void initMenuUnitType() {
        for (UnitType type : UnitType.values()) {
            MenuItem menuItem = GUIFactory.createMenuItem(type.toString());
            menuItem.setOnAction(event -> {
                menuUnitType.setText(type.toString());
                menuUnitType.setId(type.toString());
            });
            menuUnitType.getItems().add(menuItem);
        }
    }
}
