package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Battle;
import edu.ntnu.arunang.wargames.Terrain;
import edu.ntnu.arunang.wargames.gui.ArmySingleton;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.gui.factory.*;
import edu.ntnu.arunang.wargames.observer.HitObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Controller for the simulation page.
 */

public class SimulateCON {
    private final ArmySingleton armySingleton = ArmySingleton.getInstance();
    private Button btnStart;

    @FXML
    private VBox vBoxDetails;
    @FXML
    private HBox mainContainer;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox armyContainer;

    private Thread thread;
    private Army attacker = armySingleton.getAttacker().copy();
    private Army defender = armySingleton.getDefender().copy();
    private Battle battle = new Battle(attacker, defender);
    LineChart<Number, Number> barChart = ChartFactory.createBarChart(attacker, defender);
    XYChart.Series<Number, Number> attackerData;
    XYChart.Series<Number, Number> defenderData;
    private Text errorMsg;
    private Terrain terrain;
    private MenuButton menuTerrain;
    private HitObserver observer;

    /**
     * Update the gui with a winner and a loser army. Used when simulation is done.
     *
     * @param winner winning army
     * @param loser  losing army
     */

    public void updateArmies(Army winner, Army loser) {
        //clear the container before adding
        armyContainer.getChildren().clear();

        Text txtTerrain = TextFactory.createTitle("Terrain: " + terrain);
        Text txtWinner = TextFactory.createSmallTitle("Winner: " + winner.getName());
        GridPane winnerArmy = ContainerFactory.createArmyPane(winner);
        Text txtLoser = TextFactory.createSmallTitle("Loser: " + loser.getName());
        GridPane loserArmy = ContainerFactory.createArmyPane(loser);
        Text numOfAttacks = TextFactory.createSmallText("Number of attacks: " + battle.getNumOfAttacks());

        armyContainer.getChildren().addAll(txtTerrain, txtWinner, winnerArmy, txtLoser, loserArmy, numOfAttacks);
        btnStart.setText("Rollback");
        btnStart.setOnAction(this::onRollBack);
    }

    /**
     * Updates the barchart by getting the data from the armies.
     */

    public void updateBarChart(int i) {
        //add the data to the barchart
        attackerData.getData().add(ChartFactory.createData(i, attacker.size()));
        defenderData.getData().add(ChartFactory.createData(i, defender.size()));
    }

    /**
     * Update the army under simulation.
     */

    public void updateArmies() {
        //clear the container before adding
        armyContainer.getChildren().clear();

        armyContainer.getChildren().add(TextFactory.createSmallTitle(attacker.getName()));
        armyContainer.getChildren().add(ContainerFactory.createArmyPane(attacker));
        armyContainer.getChildren().add(TextFactory.createSmallTitle(defender.getName()));
        armyContainer.getChildren().add(ContainerFactory.createArmyPane(defender));
    }


    void onCleanChart(ActionEvent event) {
        barChart.getData().clear();
        createNewDataSeries();
    }

    public void createNewDataSeries() {
        attackerData = ChartFactory.createDataSeries(attacker.getName() + " " + terrain);
        defenderData = ChartFactory.createDataSeries(defender.getName() + " " + terrain);
        barChart.getData().add(attackerData);
        barChart.getData().add(defenderData);
    }

    void initBottomBar() {
        HBox hBox = NavbarFactory.createBottomBar();

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
        hBox.getChildren().addAll(btnCleanChart, errorMsg, vbox, btnFinish, btnStart);

    }

    void onChooseTerrain(ActionEvent event, Terrain terrain) {
        menuTerrain.setText(terrain.toString());
        this.terrain = terrain;
    }

    /**
     * Finishes the simulation. Redirects to the mainpage and clears the singleton.
     *
     * @param event triggering event
     */

    void onFinish(ActionEvent event) {
        armySingleton.clear();
        battle.stopSimulation();
        if (thread !=null) {
            thread.interrupt();
        }
        GUI.setSceneFromActionEvent(event, "main");
    }

    /**
     * Main process. Starts the simulation, and updates the gui accordingly.
     */

    void onStart(ActionEvent event) {
        //quit if terrain is not choosen
        if (this.terrain == null) {
            errorMsg.setText("Choose a terrain.");
            return;
        }
        errorMsg.setText("");

        observer = new HitObserver(attacker, defender, battle, this);

        try {
            thread = battle.simulate(2, terrain, this);
        } catch (IllegalStateException e) {
            AlertFactory.createError("Something went wrong! \n" + e.getMessage()).show();
        } catch (Exception e) {
            AlertFactory.createError("An unexpected error occured!\n" + e.getMessage());
        }
        createNewDataSeries();
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

    void onRestart(ActionEvent event) {
        onStart(event);
        updateArmies();
    }

    private void onRollBack(ActionEvent event) {
        attacker = armySingleton.getAttacker().copy();
        defender = armySingleton.getDefender().copy();
        battle = new Battle(attacker, defender);
        btnStart.setText("Start");
        updateArmies();
        btnStart.setOnAction(this::onStart);
    }

    /**
     * Initializes the gui elements and creates a barchart
     */

    @FXML
    void initialize() {
        //create barchart
        mainContainer.getChildren().add(barChart);

        //create bottombar
        initBottomBar();

        updateArmies();
    }
}