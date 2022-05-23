package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.event.EventType;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.container.UnitContainerManager;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.gui.factory.*;
import edu.ntnu.arunang.wargames.model.Army;
import edu.ntnu.arunang.wargames.model.battle.Battle;
import edu.ntnu.arunang.wargames.model.battle.Terrain;
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

    // gui elements
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> attackerData;
    private XYChart.Series<Number, Number> defenderData;
    private Button btnStart;
    private Button btnCleanChart;
    private ComboBox<Terrain> terrainComboBox;
    private final Label simulationText = TextFactory.createSmallText("Simulation run: " + 0);
    private final Text attackerHeader = TextFactory.createTitle("", true);
    private final Text defenderHeader = TextFactory.createTitle("", true);
    private final Label txtErrorMsg = TextFactory.createSmallText("");
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
     * Finishes the simulation. Redirects to the mainpage and clears the singleton.
     *
     * @param event triggering event
     */

    private void onFinish(ActionEvent event) {
        battle.stopSimulation();

        //close the the thread incase it is still running
        if (thread != null) {
            thread.interrupt();
        }

        GUI.setSceneFromActionEvent(event, "main");
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
     * Starts the simulation. It prepares the battle first, and creates a new thread
     * for the simulation
     */

    private void onSimulationStart(ActionEvent event) {
        // Warn the user if terrain has not been choosen
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

        battle.attach(this::onRepaint);

        thread = new Thread(() -> battle.simulateDelayWithTerrain(delay));
        thread.setDaemon(true);
        thread.start();

        initUnitsWindow(true);

        //disable terrain chooser and clean chart
        terrainComboBox.setDisable(true);
        btnCleanChart.setDisable(true);

        btnStart.setText("Stop");
        btnStart.setOnAction(this::onSimulationStop);
    }

    /**
     * Stops the simulation
     *
     * @param event triggering event
     */

    private void onSimulationStop(ActionEvent event) {
        battle.stopSimulation();
        btnStart.setText("Resume");
        btnStart.setOnAction(this::onSimulationResume);
    }

    /**
     * Helper method for restarting the simulation. Gets the armies from the singleton again and creates a new battle.
     * Updates the gui accordingly.
     */

    private void onSimulationResume(ActionEvent event) {
        //restart the thread
        thread = new Thread(() -> battle.simulateDelayWithTerrain(delay));
        thread.setDaemon(true);
        thread.start();

        btnStart.setText("Stop");
        btnStart.setOnAction(this::onSimulationStop);
    }

    /**
     * Resets the simulation page and loads a new Battle class with the original
     * attacker and defender.
     */

    private void onSimulationReset() {
        attacker = originalAttacker.copy();
        defender = originalDefender.copy();

        simulations.add(battle.copy());

        battle = new Battle(attacker, defender, null);

        battle.attach(this::onRepaint);

        //reset the start button
        btnStart.setText("Simulate");
        btnStart.setOnAction(this::onSimulationStart);

        attackerDetails.setArmy(attacker);
        defenderDetails.setArmy(defender);

        repaintArmyInformation();
        createNewDataSeries();
        resetArmyInformation();
        initUnitsWindow(false);
    }

    /**
     * Finishes the simulation by announcing the winner and loser army
     * Updates the gui elements accordingly.
     */

    public void onSimulationFinish() {
        if (attacker.hasUnits()) {
            attackerHeader.setText("Winner: " + attacker.getName());
            defenderHeader.setText("Loser: " + defender.getName());
            defenderUnitsWindow.getChildren().clear();
        } else {
            attackerHeader.setText("Loser: " + attacker.getName());
            defenderHeader.setText("Winner: " + defender.getName());
            attackerUnitsWindow.getChildren().clear();
        }

        btnStart.setText("Reset");
        btnStart.setOnAction(event -> onSimulationReset());

        btnCleanChart.setDisable(false);
        terrainComboBox.setDisable(false);

        repaintArmyInformation();
        repaintUnitInformation();
        repaintChart();
    }

    /**
     * Updates the graphical elements. Text updates every 30 ms, and the linechart updates
     * every 100ms.
     */

    public void onRepaint(EventType type) {
        long now = new Date().getTime();
        switch (type) {
            case UPDATE -> {
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

            case FINISH -> onSimulationFinish();
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
     * Create a new data series for the linechart
     */

    private void createNewDataSeries() {
        attackerData = ChartFactory.createDataSeries(attacker.getName() + ", run: " + simulations.size());
        defenderData = ChartFactory.createDataSeries(defender.getName() + ", run: " + simulations.size());
        lineChart.getData().add(attackerData);
        lineChart.getData().add(defenderData);
    }

    /**
     * Initializes the unitwindow with unit cards.
     *
     * @param condensed condesed cards or normal cards
     */

    private void initUnitsWindow(boolean condensed) {
        attackerUnitsWindow.getChildren().clear();
        defenderUnitsWindow.getChildren().clear();

        attackerUnitsWindow.getChildren().add(TextFactory.createSmallTitle("Attacker units:", false));
        defenderUnitsWindow.getChildren().add(TextFactory.createSmallTitle("Defender units:", false));

        attackerUnitsContainer = new UnitContainerManager(attacker, condensed);
        defenderUnitsContainer = new UnitContainerManager(defender, condensed);

        attackerUnitsWindow.getChildren().add(attackerUnitsContainer.getFlowpane());
        defenderUnitsWindow.getChildren().add(defenderUnitsContainer.getFlowpane());
    }

    /**
     * Initializes the bottombar with Start, Finish, Clean, Terrain chooser buttons.
     */

    private void initBottomBar() {
        btnStart = ButtonFactory.createDefaultButton("Simulate");
        btnStart.setOnAction(this::onSimulationStart);

        Button btnFinish = ButtonFactory.createDefaultButton("Main Menu");
        btnFinish.setOnAction(this::onFinish);

        btnCleanChart = ButtonFactory.createDefaultButton("Clean Chart");
        btnCleanChart.setOnAction(this::onCleanChart);

        terrainComboBox = ButtonFactory.createMenuButton(Terrain.values());
        terrainComboBox.setOnAction(actionEvent -> this.terrain = terrainComboBox.getValue());
        terrainComboBox.setPromptText("Choose a terrain");

        TextDecorator.makeErrorText(txtErrorMsg);

        VBox comboboxContainer = ContainerFactory.createCenteredVBox(300);
        comboboxContainer.getChildren().add(terrainComboBox);

        HBox bottomBar = NavbarFactory.createBottomBar(btnCleanChart, btnFinish, comboboxContainer, txtErrorMsg,
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

        battle = new Battle(this.attacker, this.defender, null);

        attackerArmyContainer.getChildren().add(attackerHeader);
        defenderArmyContainer.getChildren().add(defenderHeader);

        attackerDetails = new ArmyContainer(this.attacker);
        defenderDetails = new ArmyContainer(this.defender);
        attackerArmyContainer.getChildren().add(attackerDetails.getGridPane());
        defenderArmyContainer.getChildren().add(defenderDetails.getGridPane());

        infoContainer.getChildren().add(TextFactory.createTitle(this.attacker.getName() + " vs. " + this.defender.getName(), true));
        infoContainer.getChildren().add(simulationText);

        // create linechart
        lineChart = ChartFactory.createLineChart(this.attacker, this.defender);
        mainContainer.getChildren().add(lineChart);

        createNewDataSeries();
        resetArmyInformation();
        initUnitsWindow(false);
        initBottomBar();
    }
}