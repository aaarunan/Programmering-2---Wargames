package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.event.EventType;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.container.ArmyContainer;
import edu.ntnu.arunang.wargames.gui.container.UnitContainerManager;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.gui.factory.*;
import edu.ntnu.arunang.wargames.model.army.Army;
import edu.ntnu.arunang.wargames.model.battle.Battle;
import edu.ntnu.arunang.wargames.model.battle.Terrain;
import javafx.application.Platform;
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
    private final static int UPDATE_GRAPHICS_DELTA = 100;
    private final static int UPDATE_TEXT_DELTA = 40;
    private final int delay = 1;
    private final ArrayList<Battle> simulations = new ArrayList<>();
    private final Label simulationText = TextFactory.createSmallText("Simulation run: " + 0);
    private final Text attackerHeader = TextFactory.createTitle("", true);
    private final Text defenderHeader = TextFactory.createTitle("", true);
    private final Label txtErrorMsg = TextFactory.createSmallText("");
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox mainContainer, attackerArmyContainer, defenderArmyContainer, infoContainer, attackerUnitsWindow, defenderUnitsWindow;
    private long lastTextUpdate = new Date().getTime();
    private long lastGraphicUpdate = new Date().getTime();
    private Army originalAttacker;
    private Army originalDefender;
    private Terrain terrain;
    private Battle battle;
    // linechart
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> attackerData;
    private XYChart.Series<Number, Number> defenderData;
    private Button btnStart, btnCleanChart;
    private ComboBox<Terrain> terrainComboBox;
    private ArmyContainer attackerDetails, defenderDetails;
    private UnitContainerManager attackerUnitsContainer, defenderUnitsContainer;

    /**
     * Starts the simulation. It prepares the battle first, and creates a new thread
     * for the simulation. Lastly the btnCleanChart and
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
            battle.prepareBattle();
        } catch (IllegalStateException | IllegalArgumentException e) {
            AlertFactory.createError("Something went wrong! \n" + e.getMessage()).show();
            return;
        }

        battle.attach(this::onRepaint);

        Thread thread = new Thread(() -> battle.simulate(delay));
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
        Thread thread = new Thread(() -> battle.simulate(delay));
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
        simulations.add(battle.copy());

        battle = new Battle(originalAttacker.copy(), originalDefender.copy(), null);
        battle.attach(this::onRepaint);

        //reset the start button
        btnStart.setText("Simulate");
        btnStart.setOnAction(this::onSimulationStart);

        attackerDetails.setArmy(battle.getAttacker());
        defenderDetails.setArmy(battle.getDefender());

        repaintArmyInformation();
        repaintInfoContainer();
        createNewDataSeries();
        resetArmyInformation();
        initUnitsWindow(false);
    }

    /**
     * Finishes the simulation by announcing the winner and loser army
     * Updates the gui elements accordingly.
     */

    public void onSimulationFinish() {
        btnStart.setText("Reset");
        btnStart.setOnAction(event -> onSimulationReset());

        btnCleanChart.setDisable(false);
        terrainComboBox.setDisable(false);

        repaintArmyInformation();
        repaintUnitInformation();
        repaintInfoContainer();
        repaintChart();
    }

    /**
     * Finishes the simulation. Redirects to the mainpage and clears the singleton.
     *
     * @param event triggering event
     */

    private void onFinish(ActionEvent event) {
        battle.stopSimulation();

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
     * Updates the graphical elements. Text updates every 30 ms, and the linechart updates
     * every 100ms.
     */

    public void onRepaint(EventType type) {
        long now = new Date().getTime();
        switch (type) {
            case UPDATE -> {
                if (now - UPDATE_TEXT_DELTA >= lastTextUpdate) {
                    Platform.runLater(() -> {
                        repaintArmyInformation();
                        repaintUnitInformation();
                    });
                    lastTextUpdate = now;
                }

                if (now - UPDATE_GRAPHICS_DELTA >= lastGraphicUpdate) {
                    Platform.runLater(this::repaintChart);
                    lastGraphicUpdate = now;
                }
            }

            case FINISH -> Platform.runLater(this::onSimulationFinish);
        }

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
     * Updates the barchart by getting the data from the armies.
     */

    private void repaintChart() {
        // add the data to the barchart
        attackerData.getData().add(ChartFactory.createData(battle.getNumOfAttacks(), battle.getAttacker().size()));
        defenderData.getData().add(ChartFactory.createData(battle.getNumOfAttacks(), battle.getDefender().size()));
    }

    private void repaintInfoContainer() {
        infoContainer.getChildren().clear();

        Text information = TextFactory.createTitle(battle.getAttacker().getName() + "  vs. " + battle.getDefender().getName(), true);

        if (battle.getWinner() != null) {
            String winner = "(Loser)";
            if (battle.getAttacker().hasUnits()) {
                winner = "(Attacker)";
            }

            information.setText("Winner: " + battle.getWinner().getName() + " " + winner);

        }

        infoContainer.getChildren().add(information);
        infoContainer.getChildren().add(simulationText);
    }

    /**
     * Resets the army names and updates the simulation runs.
     */

    private void resetArmyInformation() {
        attackerHeader.setText(battle.getAttacker().getName());
        defenderHeader.setText(battle.getDefender().getName());
        simulationText.setText("Simulation run: " + simulations.size());
    }

    /**
     * Create a new data series for the linechart
     */

    private void createNewDataSeries() {
        attackerData = ChartFactory.createDataSeries(battle.getAttacker().getName() + ", run: " + simulations.size());
        defenderData = ChartFactory.createDataSeries(battle.getDefender().getName() + ", run: " + simulations.size());
        lineChart.getData().add(attackerData);
        lineChart.getData().add(defenderData);
    }

    /**
     * Initializes the gui elements and creates a barchart. The initialize method
     * must be called manually and set an attacking and defending army.
     */

    protected void initialize(Army attacker, Army defender) {
        originalDefender = defender;
        originalAttacker = attacker;

        battle = new Battle(originalAttacker.copy(), originalDefender.copy(), null);


        // create linechart
        lineChart = ChartFactory.createLineChart(battle.getAttacker(), battle.getDefender());
        mainContainer.getChildren().add(lineChart);

        repaintInfoContainer();
        createNewDataSeries();
        resetArmyInformation();
        initArmyWindows();
        initUnitsWindow(false);
        initBottomBar();
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

        attackerUnitsContainer = new UnitContainerManager(battle.getAttacker(), condensed);
        defenderUnitsContainer = new UnitContainerManager(battle.getDefender(), condensed);

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
        terrainComboBox.setOnAction(actionEvent -> terrain = terrainComboBox.getValue());
        terrainComboBox.setPromptText("Choose a terrain");

        TextDecorator.makeErrorText(txtErrorMsg);

        VBox comboboxContainer = ContainerFactory.createCenteredVBox(300);
        comboboxContainer.getChildren().add(terrainComboBox);

        HBox bottomBar = NavbarFactory.createBottomBar(btnCleanChart, btnFinish, comboboxContainer, txtErrorMsg,
                btnStart);
        borderPane.setBottom(bottomBar);
    }

    private void initArmyWindows() {
        attackerDetails = new ArmyContainer(battle.getAttacker());
        defenderDetails = new ArmyContainer(battle.getDefender());

        attackerArmyContainer.getChildren().add(attackerHeader);
        defenderArmyContainer.getChildren().add(defenderHeader);
        attackerArmyContainer.getChildren().add(attackerDetails.getGridPane());
        defenderArmyContainer.getChildren().add(defenderDetails.getGridPane());
    }
}