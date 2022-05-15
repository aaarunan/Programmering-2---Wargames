package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Terrain;
import edu.ntnu.arunang.wargames.battle.AttackObserver;
import edu.ntnu.arunang.wargames.battle.Battle;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.gui.factory.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * Controller for the simulation page.
 */

public class SimulateCON {

    @FXML
    private VBox mainContainer;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox attackerArmyContainer;
    @FXML
    private VBox defenderArmyContainer;
    @FXML
    private VBox infoContainer;

    private Thread thread;

    private Army attacker;
    private Army defender;
    private Army originalAttacker;
    private Army originalDefender;

    private Terrain terrain;

    private Battle battle;
    private ArrayList<Battle> simulations = new ArrayList<>();

    private AttackObserver attackObserver;

    //gui elements
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> attackerData;
    private XYChart.Series<Number, Number> defenderData;
    private Button btnStart;
    private Button btnCleanChart;
    private ComboBox<Terrain> terrainComboBox;

    private Text simulationText = TextFactory.createSmallText("Simulation run: " + 0);
    private Text attackerHeader;
    private Text defenderHeader;
    private Text txtErrorMsg;

    private ArmyContainer attackerDetails;
    private ArmyContainer defenderDetails;

    private long lastTextUpdate = new Date().getTime();
    private long lastGraphicUpdate = new Date().getTime();

    private int updateGraphicsDelta = 40;
    private int updateTextDelta = 100;

    /**
     * Updates the barchart by getting the data from the armies.
     */

    private void repaintChart() {
        //add the data to the barchart
        attackerData.getData().add(ChartFactory.createData(battle.getNumOfAttacks(), attacker.size()));
        defenderData.getData().add(ChartFactory.createData(battle.getNumOfAttacks(), defender.size()));
    }

    /**
     * Update the army under simulation.
     */

    private void repaintDetails() {
        //clear the container before adding
        attackerDetails.updateData(attacker);
        defenderDetails.updateData(defender);
    }


    private void onCleanChart(ActionEvent event) {
        lineChart.getData().clear();
        createNewDataSeries();
    }

    private void createNewDataSeries() {
        attackerData = ChartFactory.createDataSeries(attacker.getName() + ", run: " + simulations.size());
        defenderData = ChartFactory.createDataSeries(defender.getName() + ", run: " + simulations.size());
        lineChart.getData().add(attackerData);
        lineChart.getData().add(defenderData);
    }

    private void initBottomBar() {

        btnStart = ButtonFactory.createDefaultButton("Start");
        btnStart.setOnAction(this::onStart);

        Button btnFinish = ButtonFactory.createDefaultButton("Finish");
        btnFinish.setOnAction(this::onFinish);

        btnCleanChart = ButtonFactory.createDefaultButton("Clean");
        btnCleanChart.setOnAction(this::onCleanChart);

        terrainComboBox = ButtonFactory.createMenuButton(Terrain.values());
        terrainComboBox.setOnAction(actionEvent -> this.terrain = terrainComboBox.getValue());
        terrainComboBox.setPromptText("Choose a terrain");

        txtErrorMsg = TextFactory.createSmallText("");
        TextDecorator.makeErrorText(txtErrorMsg);

        VBox comboboxContainer = ContainerFactory.createCenteredVBox(300);
        comboboxContainer.getChildren().add(terrainComboBox);

        HBox bottomBar = NavbarFactory.createBottomBar(btnCleanChart, comboboxContainer, btnFinish, txtErrorMsg, btnStart);
        borderPane.setBottom(bottomBar);
    }


    /**
     * Finishes the simulation. Redirects to the mainpage and clears the singleton.
     *
     * @param event triggering event
     */

    private void onFinish(ActionEvent event) {
        battle.stopSimulation();
        battle.detach(attackObserver);
        if (thread != null) {
            thread.interrupt();
        }
        GUI.setSceneFromActionEvent(event, "main");
    }

    /**
     * Main process. Starts the simulation, and updates the gui accordingly.
     */

    private void onStart(ActionEvent event) {
        //quit if terrain is not choosen
        if (this.terrain == null) {
            txtErrorMsg.setText("Choose a terrain.");
            return;
        }
        txtErrorMsg.setText("");

        try {
            battle.setTerrain(terrain);
            thread = new Thread(() -> battle.simulateDelayWithTerrain(2));
            thread.start();
        } catch (IllegalStateException | IllegalArgumentException e) {
            AlertFactory.createError("Something went wrong! \n" + e.getMessage()).show();
            return;
        } catch (Exception e) {
            AlertFactory.createError("An unexpected error occured!\n" + e.getMessage()).show();
            return;
        }

        terrainComboBox.setDisable(true);
        btnCleanChart.setDisable(true);

        btnStart.setText("Stop");
        btnStart.setOnAction(this::onStop);
    }

    private void onStop(ActionEvent event) {
        battle.stopSimulation();

        btnStart.setText("Restart");
        btnStart.setOnAction(this::onRestart);
    }

    /**
     * Helper method for restarting the simulation. Gets the armies
     * from the singleton again and creates a new battle. Updates the gui
     * accordingly.
     */

    private void onRestart(ActionEvent event) {
        battle.continueSimulation();
    }

    private void onRollBack(ActionEvent event) {
        attacker = originalAttacker.copy();
        defender = originalDefender.copy();

        simulations.add(battle.copy());
        battle = new Battle(attacker, defender, null);
        battle.attach(attackObserver);

        btnStart.setText("Start");
        btnStart.setOnAction(this::onStart);

        repaintDetails();
        createNewDataSeries();
        resetArmyInformation();
    }

    public void onSimulationFinish() {
        if (attacker.hasUnits()) {
            attackerHeader.setText("Winner: " + attacker.getName());
            defenderHeader.setText("Loser: " + defender.getName());
        } else {
            attackerHeader.setText("Loser: " + attacker.getName());
            defenderHeader.setText("Winner: " + defender.getName());
        }

        btnStart.setText("Rollback");
        btnStart.setOnAction(this::onRollBack);

        btnCleanChart.setDisable(false);
        terrainComboBox.setDisable(false);

        repaintDetails();
    }

    public void onRepaint() {
        long now = new Date().getTime();

        if (now - updateTextDelta >= lastTextUpdate) {
            repaintDetails();
            lastTextUpdate = now;
        }

        if (now - updateGraphicsDelta >= lastGraphicUpdate) {
            repaintChart();
            lastGraphicUpdate = now;
        }
    }

    private void resetArmyInformation() {
        attackerHeader.setText(attacker.getName());
        defenderHeader.setText(defender.getName());
        simulationText.setText("Simulation run: " + simulations.size());
    }

    protected void setDefender(Army defender) {
        this.originalDefender = defender;
    }

    protected void setAttacker(Army attacker) {
        this.originalAttacker = attacker;
    }

    /**
     * Initializes the gui elements and creates a barchart
     */

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            attacker = originalAttacker.copy();
            defender = originalDefender.copy();

            attackerHeader = TextFactory.createSmallTitle("");
            defenderHeader = TextFactory.createSmallTitle("");

            attackerArmyContainer.getChildren().add(attackerHeader);
            defenderArmyContainer.getChildren().add(defenderHeader);

            attackerDetails = new ArmyContainer(attacker);
            defenderDetails = new ArmyContainer(defender);
            attackerArmyContainer.getChildren().add(attackerDetails.getGridPane());
            defenderArmyContainer.getChildren().add(defenderDetails.getGridPane());

            attackObserver = new AttackObserver(this);
            battle = new Battle(attacker, defender, null);
            battle.attach(attackObserver);

            simulationText = TextFactory.createSmallText("");
            infoContainer.getChildren().add(TextFactory.createTitle(attacker.getName() + " vs. " + defender.getName()));
            infoContainer.getChildren().add(simulationText);

            //create barchart
            lineChart = ChartFactory.createBarChart(attacker, defender);
            mainContainer.getChildren().add(lineChart);
            createNewDataSeries();

            resetArmyInformation();

            //create bottombar
            initBottomBar();
        });
    }
}