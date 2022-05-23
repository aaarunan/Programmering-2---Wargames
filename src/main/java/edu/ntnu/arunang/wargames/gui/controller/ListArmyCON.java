package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.StateHandler;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.container.UnitContainerManager;
import edu.ntnu.arunang.wargames.gui.decorator.ButtonDecorator;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.gui.factory.AlertFactory;
import edu.ntnu.arunang.wargames.gui.factory.ButtonFactory;
import edu.ntnu.arunang.wargames.gui.factory.NavbarFactory;
import edu.ntnu.arunang.wargames.gui.factory.TextFactory;
import edu.ntnu.arunang.wargames.gui.util.ArmyFSHutil;
import edu.ntnu.arunang.wargames.model.army.Army;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

/**
 * Controller for pages where armies are listed.
 */

public class ListArmyCON {
    @FXML
    private VBox armyListContainer;
    @FXML
    private Text txtArmyName;
    @FXML
    private VBox informationContainer;
    @FXML
    private VBox unitDetailsContainer;
    @FXML
    private Text title;
    @FXML
    private VBox armyDetailsContainer;
    @FXML
    private BorderPane borderPane;

    private Army pickedArmy = new Army("army");
    private Army choosenAttacker;

    private Label txtErrorMsg;

    private Button btnPressedArmy;
    private boolean isAttacker;
    private ArmyContainer unitContainer;

    private void onArmyChosen(Button button, File file) {
        pickedArmy = ArmyFSHutil.loadArmyFromFile(file);
        if (pickedArmy == null) {
            return;
        }

        updatePressedArmy(button);
        repaintArmyInformation();
    }

    /**
     * Method called when the user presses the continue button. It sets the attacking army and defending army.
     *
     * @param event triggered by continue button
     */

    void onContinue(ActionEvent event) {
        if (pickedArmy == null) {
            txtErrorMsg.setText("Choose an army");
            return;
        }

        if (choosenAttacker == null) {
            choosenAttacker = pickedArmy;
            ButtonDecorator.makeListElementActive(btnPressedArmy);
            btnPressedArmy.setText("(Attacker) " + btnPressedArmy.getText());
            btnPressedArmy = null;
        } else {
            Army choosenDefender = pickedArmy;
            FXMLLoader loader = GUI.initLoader(GUI.getPath("simulate"));
            ((SimulateCON) loader.getController()).initialize(choosenAttacker, choosenDefender);
            Scene scene = btnPressedArmy.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(loader.getRoot(), scene.getWidth(), scene.getHeight()));
        }
        repaintHeader();
        informationContainer.setVisible(false);
        pickedArmy = null;
    }

    /**
     * Deletes an Army from the Singleton and in the stored files in /resources/army folder. The method will show a
     * pop-up warning that the user must confirm.
     */

    @FXML
    void onDelete() {
        ArmyFSH armyFSH = new ArmyFSH();
        // get the result of the popup
        Optional<ButtonType> result = AlertFactory.createConfirmation("Are you sure you want to delete army?")
                .showAndWait();

        if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
            return;
        }

        // show error if failed
        if (!armyFSH.deleteArmy(pickedArmy)) {
            AlertFactory.createWarning("Could not remove army. \n The army file might be in use or does not exist.")
                    .show();
            return;
        }

        // refresh page
        repaintArmies();

        // hides the details window
        informationContainer.setVisible(false);
    }

    /**
     * Updates the details container. If the window is disabled it will be set to enabled.
     * <p>
     * It creates a detailed gridPane of the army stats, and a tableview of with every unit.
     */

    void repaintArmyInformation() {
        if (!informationContainer.isVisible()) {
            informationContainer.setVisible(true);
        }

        txtArmyName.setText(pickedArmy.getName());

        unitContainer.setArmy(pickedArmy);
        unitContainer.updateData();

        unitDetailsContainer.getChildren().clear();
        unitDetailsContainer.getChildren().add(new UnitContainerManager(pickedArmy, false).getFlowpane());
    }

    /**
     * Update the army list.
     */

    void repaintArmies() {
        // clear the container before adding
        armyListContainer.getChildren().clear();

        ArmyFSH armyFSH = new ArmyFSH();

        File[] armyFiles = armyFSH.getAllArmyFiles();

        if (armyFiles == null || armyFiles.length == 0) {
            armyListContainer.getChildren().add(TextFactory.createSmallTitle("No armies found!", false));
            return;
        }


        // loop through all the armies
        for (File file : armyFiles) {
            Button armyButton = ButtonFactory.listButton(armyFSH.getFileNameWithoutExtension(file));
            armyButton.setOnAction(buttonEvent -> onArmyChosen(armyButton, file));
            armyListContainer.getChildren().add(armyButton);
        }
    }


    /**
     * Update the header. This is according to whether simulation has been chosen and the attacker is not null.
     */

    void repaintHeader() {
        if (StateHandler.getInstance().isSimulate()) {
            if (choosenAttacker == null) {
                title.setText("Choose attacker");
            } else {
                title.setText("Choose defender");
            }
        }
    }

    /**
     * Changes the status when an army is pressed. When the army is selected it will be highlighted, and the previous
     * selected army will be set to default styling.
     *
     * @param button army that was pressed.
     */

    void updatePressedArmy(Button button) {
        if (btnPressedArmy != null && !isAttacker) {
            ButtonDecorator.makeListElementDefault(btnPressedArmy);
        }

        if (!pickedArmy.equals(choosenAttacker)) {
            ButtonDecorator.makeListElementHighlighted(button);
            isAttacker = false;
        } else {
            isAttacker = true;
        }

        btnPressedArmy = button;
    }

    /**
     * Import an army from the file system. The file will be re-saved in the folder where armies are
     * stored by the ArmyFSH default.
     *
     * @param actionEvent triggering event
     */

    private void importArmy(ActionEvent actionEvent) {
        //get the file that is being imported
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        //check that a file has been chosen
        if (file == null) {
            return;
        }

        //try to load the file to an army
        pickedArmy = ArmyFSHutil.loadArmyFromFile(file);
        if (pickedArmy == null) {
            return;
        }

        //Write the army
        if (!ArmyFSHutil.writeArmy(pickedArmy)) {
            return;
        }

        repaintArmies();

        AlertFactory.createInformation("Import successful").show();
    }

    /**
     * Initializes the page. It repaints the header and initializes the bottom
     * bar.
     */

    @FXML
    private void initialize() {
        repaintHeader();
        initBottomBar();

        informationContainer.setVisible(false);
        unitContainer = new ArmyContainer(pickedArmy);
        armyDetailsContainer.getChildren().add(unitContainer.getGridPane());

        repaintArmies();
    }

    /**
     * Initialize the bottom bar. Buttons will be added according to whether simulation has been chosen.
     */

    void initBottomBar() {
        HBox bottomBar = NavbarFactory.createBottomBar();
        // create back button
        Button btnBack = ButtonFactory.createDefaultButton("Back");
        btnBack.setOnAction(event -> GUI.setSceneFromActionEvent(event, "main"));
        txtErrorMsg = TextFactory.createSmallText("");
        TextDecorator.makeErrorText(txtErrorMsg);

        bottomBar.getChildren().addAll(txtErrorMsg, btnBack);

        // add the buttons to the bottom bar
        borderPane.setBottom(bottomBar);

        // Add button to whether or not the page is for choosing armies for simulation
        if (StateHandler.getInstance().isSimulate()) {
            Button btnContinue = ButtonFactory.createDefaultButton("Continue");
            btnContinue.setOnAction(this::onContinue);
            bottomBar.getChildren().add(btnContinue);

            return;
        }

        Button btnNewArmy = ButtonFactory.createDefaultButton("New army");
        btnNewArmy.setOnAction(event -> GUI.setSceneFromActionEvent(event, "newArmy"));

        Button btnImportArmy = ButtonFactory.createDefaultButton("Import army");
        btnImportArmy.setOnAction(this::importArmy);

        bottomBar.getChildren().addAll(btnImportArmy, btnNewArmy);

    }
}
