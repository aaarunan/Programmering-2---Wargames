package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.fsh.ArmyFSH;
import edu.ntnu.arunang.wargames.fsh.FileFormatException;
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
import java.io.IOException;
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

    private Army army = new Army("army");
    private Army attacker;
    private Army defender;
    private Label txtErrorMsg;

    private Button btnPressedArmy;
    private boolean isAttacker;
    private ArmyContainer unitContainer;

    /**
     * Updates the details container. If the window is disabled it will be set to enabled.
     * <p>
     * It creates a detailed gridPane of the army stats, and a tableview of with every unit.
     */

    void repaintArmyInformation() {
        if (!informationContainer.isVisible()) {
            informationContainer.setVisible(true);
        }

        txtArmyName.setText(army.getName());

        unitContainer.setArmy(army);
        unitContainer.updateData();

        unitDetailsContainer.getChildren().clear();
        unitDetailsContainer.getChildren().add(new UnitContainerManager(army, false).getFlowpane());
    }

    /**
     * Update the army list.
     *
     * @param armyFiles list of armies
     */

    void repaintArmies(File[] armyFiles) {
        // clear the container before adding
        armyListContainer.getChildren().clear();

        if (armyFiles == null || armyFiles.length == 0) {
            armyListContainer.getChildren().add(TextFactory.createSmallTitle("No armies found!", false));
            return;
        }


        ArmyFSH armyFSH = new ArmyFSH();

        // loop through all the armies
        for (File file : armyFiles) {
            Button button = ButtonFactory.listButton(file.getName().substring(0, file.getName().lastIndexOf('.')));
            button.setOnAction(buttonEvent -> {
                try {
                    this.army = armyFSH.loadFromFile(file);
                } catch (IOException e) {
                    AlertFactory.createError("Army could not be loaded...\n" + e.getMessage()).show();
                    return;
                } catch (FileFormatException e) {
                    AlertFactory.createError("Army is wrongly formatted! \n" + e.getMessage()).show();
                    return;
                }
                updatePressedArmy(button);
                repaintArmyInformation();
            });
            armyListContainer.getChildren().add(button);
        }
    }

    /**
     * Update the header. This is according to whether simulation has been chosen and the attacker is not null.
     */

    void repaintHeader() {
        if (StateHandler.getInstance().isSimulate()) {
            if (attacker == null) {
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

        if (!army.equals(attacker)) {
            ButtonDecorator.makeListElementHighlighted(button);
            isAttacker = false;
        } else {
            isAttacker = true;
        }

        btnPressedArmy = button;
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

    /**
     * Import an army from the file system. The file will be re-saved in the folder where armies are
     * stored by the ArmyFSH default.
     *
     * @param actionEvent triggering event
     */

    private void importArmy(ActionEvent actionEvent) {
        ArmyFSH armyFSH = new ArmyFSH();

        //get the file that is being imported
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getSource()).getScene().getWindow());

        //check that a file has been chosen
        if (file == null) {
            return;
        }

        Army army;
        //try to load the file to an army
        try {
            army = armyFSH.loadFromFile(file);
        } catch (FileFormatException e) {
            AlertFactory.createError("The file is wrongly formatted! \n" + e.getMessage()).show();
            return;
        } catch (IOException e) {
            AlertFactory.createError("An error occurred when opening the file!\n" + e.getMessage()).show();
            return;
        }

        //check if the file already exists
        if (armyFSH.fileExists(file)) {
            Optional<ButtonType> result = AlertFactory
                    .createConfirmation("This army already exists, do you want to override it?").showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.YES) {
                return;
            }
        }
        try {
            armyFSH.writeArmy(army);
        } catch (IOException e) {
            AlertFactory.createError("An error occurred when writing the army!\n" + e.getMessage()).show();
            return;
        }

        repaintArmies(armyFSH.getAllArmyFiles());
        AlertFactory.createInformation("Import successful").show();
    }

    /**
     * Method called when the user presses the continue button. It sets the attacking army and defending army.
     *
     * @param event triggered by continue button
     */

    void onContinue(ActionEvent event) {
        if (army == null) {

            txtErrorMsg.setText("Choose an army");
            return;
        }

        if (attacker == null) {
            attacker = army;
            ButtonDecorator.makeListElementActive(btnPressedArmy);
            btnPressedArmy.setText("(Attacker) " + btnPressedArmy.getText());
            btnPressedArmy = null;
        } else {
            defender = army;
            FXMLLoader loader = GUI.initLoader(GUI.getPath("simulate"));
            ((SimulateCON) loader.getController()).initialize(attacker, defender);
            Scene scene = btnPressedArmy.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(loader.getRoot(), scene.getWidth(), scene.getHeight()));
        }
        repaintHeader();
        informationContainer.setVisible(false);
        army = null;
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
        if (!armyFSH.deleteArmy(army)) {
            AlertFactory.createWarning("Could not remove army. \n The army file might be in use or does not exist.")
                    .show();
            return;
        }

        // refresh page
        File[] armyFiles = armyFSH.getAllArmyFiles();
        repaintArmies(armyFiles);

        // hides the details window
        informationContainer.setVisible(false);
    }

    /**
     * Initializes the page. It repaints the header and initializes the bottom
     * bar.
     */

    @FXML
    void initialize() {
        repaintHeader();
        initBottomBar();

        informationContainer.setVisible(false);
        unitContainer = new ArmyContainer(army);
        armyDetailsContainer.getChildren().add(unitContainer.getGridPane());

        ArmyFSH armyFSH = new ArmyFSH();
        repaintArmies(armyFSH.getAllArmyFiles());
    }
}
