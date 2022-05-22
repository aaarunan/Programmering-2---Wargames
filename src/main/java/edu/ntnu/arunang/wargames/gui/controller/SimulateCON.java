package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.container.UnitContainerManager;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.gui.factory.*;
import edu.ntnu.arunang.wargames.model.Army;
import edu.ntnu.arunang.wargames.model.battle.AttackListener;
import edu.ntnu.arunang.wargames.model.battle.Battle;
import edu.ntnu.arunang.wargames.model.battle.Terrain;
import edu.ntnu.arunang.wargames.event.gui.factory.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

    private final ArrayList<Battle> simulations = new ArrayList<>();
    private final int delay = 1;
    private final int updateGraphicsDelta = 100;
    private final int updateTextDelta = 40;
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
    @FXML
    private VBox attackerUnitsWindow, defenderUnitsWindow;
    private Thread thread;
    private Army attacker;
    private Army defender;
    private Army originalAttacker;
    private Army originalDefender;
    private Terrain terrain;
    private Battle battle;
    private AttackListener attackObserver;
    // gui elements
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> attackerData;
    private XYChart.Series<Number, Number> defenderData;
    private Button btnStart;
    private Button btnCleanChart;
    private ComboBox<Terrain> terrainComboBox;
    private Label simulationText = TextFactory.createSmallText("Simulation run: " + 0);
    private Text attackerHeader;
    private Text defenderHeader;
    private Label txtErrorMsg;
    private ArmyContainer attackerDetails;
    private ArmyContainer defenderDetails;
    private UnitContainerManager attackerUnitsContainer;
    private UnitContainerManager defenderUnitsContainer;
    private long lastTextUpdate = new Date().getTime();
    private long lastGraphicUpdate = new Date().getTime();

    /**
     * Updates the barchart by getting the data from the armies.
     */

    private void repaintChart() {
        // add the data to the barchart
        attackerData.getData().add(ChartFactory.createData(battle.getNumOfAttacks(), attacker.size()));
        defenderData.getData().add(ChartFactory.createData(battle.getNumOfAttacks(), defender.size()));
    }

    /**
     * Update the army under simulation.
     */

    private void repaintArmyInformation() {
        // clear the container before adding
        attackerDetails.updateData();
        defenderDetails.updateData();
    }

    /**
     * Repaints the unit cards
     */

    private void repaintUnitInformation() {
        attackerUnitsContainer.updateContainer();
        defenderUnitsContainer.updateContainer();
    }

    /**
     * Clears the chart and creates a new data series
     *
     * @param event triggering event
     */

    private void onCleanChart(ActionEvent event) {
        lineChart.getData().clear();
        createNewDataSeries();
    }

    /**
     * Create a new data series for the linechart
     */

    private void createNewDataSeries() {
        attackerData = ChartFactory.createDataSeries(attacker.getName() + ", run: " + simulations.size());
        defenderData = ChartFactory.createDataSeries(defender.getName() + ", run: " + simulations.size());
        lineChart.getData().add(attackerData);
        lineChart.getData().add(defenderData);
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
        // quit if terrain is not choosen
        if (this.terrain == null) {
            txtErrorMsg.setText("Choose a terrain.");
            return;
        }
        txtErrorMsg.setText("");
        battle.setTerrain(terrain);

        try {
            battle.prepareBattle(true);
        } catch (IllegalStateException | IllegalArgumentException e) {
            AlertFactory.createError("Something went wrong! \n" + e.getMessage()).show();
            return;
        }

        thread = new Thread(() -> battle.simulateDelayWithTerrain(delay));
        thread.setDaemon(true);
        thread.start();
        attackerUnitsContainer = new UnitContainerManager(attacker, true);
        defenderUnitsContainer = new UnitContainerManager(defender, true);

        attackerUnitsWindow.getChildren().clear();
        defenderUnitsWindow.getChildren().clear();

        attackerUnitsWindow.getChildren().add(attackerUnitsContainer.getFlowpane());
        defenderUnitsWindow.getChildren().add(defenderUnitsContainer.getFlowpane());

        terrainComboBox.setDisable(true);
        btnCleanChart.setDisable(true);

        btnStart.setText("Stop");
        btnStart.setOnAction(this::onStop);
    }

    /**
     * Stops the simulation
     *
     * @param event triggering event
     */

    private void onStop(ActionEvent event) {
        battle.stopSimulation();
        btnStart.setText("Restart");
        btnStart.setOnAction(this::onRestart);
    }

    /**
     * Helper method for restarting the simulation. Gets the armies from the singleton again and creates a new battle.
     * Updates the gui accordingly.
     */

    private void onRestart(ActionEvent event) {
        thread = new Thread(() -> battle.simulateDelayWithTerrain(delay));
        thread.start();
        btnStart.setText("Stop");
        btnStart.setOnAction(this::onStop);
    }

    /**
     * Resets the simulation page and loads a new Battle class with the original
     * attacker and defender.
     *
     * @param event triggering event
     */

    private void onRollBack(ActionEvent event) {
        attacker = originalAttacker.copy();
        defender = originalDefender.copy();

        simulations.add(battle.copy());
        battle = new Battle(attacker, defender, null);
        battle.attach(attackObserver);

        btnStart.setText("Start");
        btnStart.setOnAction(this::onStart);

        attackerDetails.setArmy(attacker);
        defenderDetails.setArmy(defender);

        repaintArmyInformation();
        createNewDataSeries();
        resetArmyInformation();
        initUnitsWindow();
    }

    /**
     * Finishes the simulation by announcing the winner and loser army
     * Updates the gui elements accordingly.
     */

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

        repaintArmyInformation();
        repaintUnitInformation();
    }

    /**
     * Updates the graphical elements. Text updates every 30 ms, and the linechart updates
     * every 100ms.
     */

    public void onRepaint() {
        long now = new Date().getTime();

        if (now - updateTextDelta >= lastTextUpdate) {
            repaintArmyInformation();
            repaintUnitInformation();
            lastTextUpdate = now;
        }

        if (now - updateGraphicsDelta >= lastGraphicUpdate) {
            repaintChart();
            lastGraphicUpdate = now;
        }
    }

    /**
     * Resets the army names and updates the simulation runs.
     */

    private void resetArmyInformation() {
        attackerHeader.setText(attacker.getName());
        defenderHeader.setText(defender.getName());
        simulationText.setText("Simulation run: " + simulations.size());
    }

    /**
     * Initializes the unit cards
     */

    private void initUnitsWindow() {
        attackerUnitsContainer = new UnitContainerManager(attacker, false);
        defenderUnitsContainer = new UnitContainerManager(defender, false);

        attackerUnitsWindow.getChildren().add(attackerUnitsContainer.getFlowpane());
        defenderUnitsWindow.getChildren().add(defenderUnitsContainer.getFlowpane());
    }

    /**
     * Initializes the bottombar with Start, Finish, Clean, Terrain chooser buttons.
     */

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

        HBox bottomBar = NavbarFactory.createBottomBar(btnCleanChart, comboboxContainer, btnFinish, txtErrorMsg,
                btnStart);
        borderPane.setBottom(bottomBar);
    }

    /**
     * Initializes the gui elements and creates a barchart. The initialize method
     * must be called manually and set an attacking and defending army.
     */

    protected void initialize(Army attacker, Army defender) {
        originalDefender = defender;
        originalAttacker = attacker;

        this.attacker = originalAttacker.copy();
        this.defender = originalDefender.copy();

        attackerHeader = TextFactory.createSmallTitle("", false);
        defenderHeader = TextFactory.createSmallTitle("", false);

        attackerArmyContainer.getChildren().add(attackerHeader);
        defenderArmyContainer.getChildren().add(defenderHeader);

        attackerDetails = new ArmyContainer(this.attacker);
        defenderDetails = new ArmyContainer(this.defender);
        attackerArmyContainer.getChildren().add(attackerDetails.getGridPane());
        defenderArmyContainer.getChildren().add(defenderDetails.getGridPane());

        attackObserver = new AttackListener(this);
        battle = new Battle(this.attacker, this.defender, null);
        battle.attach(attackObserver);

        simulationText = TextFactory.createSmallText("");
        infoContainer.getChildren().add(TextFactory.createTitle(this.attacker.getName() + " vs. " + this.defender.getName(), true));
        infoContainer.getChildren().add(simulationText);

        // create barchart
        lineChart = ChartFactory.createLineChart(this.attacker, this.defender);
        mainContainer.getChildren().add(lineChart);

        createNewDataSeries();
        resetArmyInformation();

        attackerArmyContainer.getChildren().add(TextFactory.createSmallTitle("Attacker units:", false));
        defenderArmyContainer.getChildren().add(TextFactory.createSmallTitle("Defender units:", false));

        initUnitsWindow();

        // create bottombar
        initBottomBar();
    }
}