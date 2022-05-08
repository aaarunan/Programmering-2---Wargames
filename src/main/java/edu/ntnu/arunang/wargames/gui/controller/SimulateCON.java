package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Terrain;
import edu.ntnu.arunang.wargames.battle.AttackObserver;
import edu.ntnu.arunang.wargames.battle.Battle;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.gui.factory.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
    private HBox infoContainer;

    private Thread thread;

    private Army attacker;
    private Army defender;
    private Army originalAttacker;
    private Army originalDefender;

    private Terrain terrain;
    private Battle battle;
    private ArrayList<Battle> simulations = new ArrayList<>();
    private AttackObserver attackObserver;

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> attackerData;
    private XYChart.Series<Number, Number> defenderData;
    private Text errorMsg;
    private MenuButton menuTerrain;
    private Button btnStart;

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

    private void repaintArmies() {
        //clear the container before adding
        defenderArmyContainer.getChildren().clear();
        attackerArmyContainer.getChildren().clear();

        attackerArmyContainer.getChildren().add(TextFactory.createSmallTitle(attacker.getName()));
        attackerArmyContainer.getChildren().add(ContainerFactory.createArmyPane(attacker));
        defenderArmyContainer.getChildren().add(TextFactory.createSmallTitle(defender.getName()));
        defenderArmyContainer.getChildren().add(ContainerFactory.createArmyPane(defender));
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
        ButtonBar hBox = NavbarFactory.createBottomBar();

        btnStart = ButtonFactory.createDefaultButton("Start");
        btnStart.setOnAction(this::onStart);

        Button btnFinish = ButtonFactory.createDefaultButton("Finish");
        btnFinish.setOnAction(this::onFinish);

        Button btnCleanChart = ButtonFactory.createDefaultButton("Clean");
        btnCleanChart.setOnAction(this::onCleanChart);

        borderPane.setBottom(hBox);
        menuTerrain = ButtonFactory.createMenuButton("Choose terrain");

        for (Terrain terrain : Terrain.values()) {
            MenuItem menuItem = ButtonFactory.createMenuItem(terrain.toString());
            menuItem.setOnAction(event -> onChooseTerrain(event, terrain));
            menuTerrain.getItems().add(menuItem);
        }

        errorMsg = TextFactory.createSmallText("");
        TextDecorator.makeErrorText(errorMsg);
        VBox vbox = ContainerFactory.createCenteredVBox(300);
        vbox.getChildren().add(menuTerrain);
        hBox.getButtons().addAll(btnCleanChart, errorMsg, vbox, btnFinish, btnStart);

    }

    private void onChooseTerrain(ActionEvent event, Terrain terrain) {
        menuTerrain.setText(terrain.toString());
        this.terrain = terrain;
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
            errorMsg.setText("Choose a terrain.");
            return;
        }
        errorMsg.setText("");

        try {
            thread = new Thread(() -> {
                battle.simulate(2, terrain);
            });
            thread.start();
        } catch (IllegalStateException e) {
            AlertFactory.createError("Something went wrong! \n" + e.getMessage()).show();
        } catch (Exception e) {
            AlertFactory.createError("An unexpected error occured!\n" + e.getMessage());
        }

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
        onStart(event);
        repaintArmies();
    }

    protected void setAttacker(Army attacker) {
        this.originalAttacker = attacker;
    }

    protected void setDefender(Army defender) {
        this.originalDefender = defender;
    }

    private void onRollBack(ActionEvent event) {
        attacker = originalAttacker.copy();
        defender = originalDefender.copy();

        simulations.add(battle.copy());
        battle = new Battle(attacker, defender);
        battle.attach(attackObserver);
        btnStart.setText("Start");
        repaintArmies();
        createNewDataSeries();
        btnStart.setOnAction(this::onStart);
    }

    public void onRepaint() {
        long now = new Date().getTime();
        if (now - updateTextDelta >= lastTextUpdate) {
            repaintArmies();
            lastTextUpdate = now;
        }
        if (now - updateGraphicsDelta >= lastGraphicUpdate) {
            repaintChart();
            lastGraphicUpdate = now;
        }
    }

    public void onSimulationFinish() {
        btnStart.setText("Rollback");
        btnStart.setOnAction(this::onRollBack);
        repaintArmies();
    }

    /**
     * Initializes the gui elements and creates a barchart
     */

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            attacker = originalAttacker.copy();
            defender = originalDefender.copy();
            battle = new Battle(attacker, defender);
            attackObserver = new AttackObserver(this);
            battle.attach(attackObserver);

            //create barchart
            lineChart = ChartFactory.createBarChart(attacker, defender);
            mainContainer.getChildren().add(lineChart);
            createNewDataSeries();

            //create bottombar
            initBottomBar();
            repaintArmies();
        });
    }
}