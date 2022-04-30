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
import javafx.geometry.Pos;
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
    @FXML
    private Button btnFinish;
    @FXML
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

    private Text errorMsg;

    private Terrain terrain;
    private MenuButton menuTerrain;

    private Battle battle = new Battle(attacker, defender);

    private HitObserver observer;

    LineChart<Number, Number> barChart = ChartFactory.createBarChart(attacker, defender);

    XYChart.Series<Number, Number> attackerData = barChart.getData().get(0);
    XYChart.Series<Number, Number> defenderData = barChart.getData().get(1);

    /**
     * Finishes the simulation. Redirects to the mainpage and clears the singleton.
     *
     * @param event triggering event
     */

    void onFinish(ActionEvent event) {
        armySingleton.clear();
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

        //logic for whether or not the button is restart or start
        if (btnStart.getText().equals("Start")) {
            btnStart.setText("Rollback");
        } else if (btnStart.getText().equals("Rollback")) {
            restart();
            btnStart.setText("Start");
            return;
        }

        observer = new HitObserver(attacker, defender, battle, this);

        battle.simulate(4, terrain, this);
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

    /**
     * Updates the barchart by getting the data from the armies.
     */

    public void updateBarChart(int i) {
        //add the data to the barchart
        attackerData.getData().add(new XYChart.Data<>(i, attacker.size()));
        defenderData.getData().add(new XYChart.Data<>(i, defender.size()));
    }

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

    /**
     * Helper method for restarting the simulation. Gets the armies
     * from the singleton again and creates a new battle. Updates the gui
     * accordingly.
     */

    void restart() {
        attacker = armySingleton.getAttacker().copy();
        defender = armySingleton.getDefender().copy();
        battle = new Battle(attacker, defender);
        updateArmies();
        updateBarChart(0);
    }

    void initBottomBar() {
        HBox hBox = ContainerFactory.createBottomBar();
        btnStart = ButtonFactory.createDefaultButton("Start");
        btnStart.setOnAction(this::onStart);
        btnFinish = ButtonFactory.createDefaultButton("Finish");
        btnFinish.setOnAction(event -> GUI.setSceneFromActionEvent(event, "main"));
        borderPane.setBottom(hBox);
        menuTerrain = ButtonFactory.createMenuButton("Choose terrain");
        for (Terrain terrain : Terrain.values()) {
            MenuItem menuItem = ButtonFactory.createMenuItem(terrain.toString());
            menuItem.setOnAction(event -> {
                menuTerrain.setText(terrain.toString());
                this.terrain = terrain;
            });
            menuTerrain.getItems().add(menuItem);
        }

        errorMsg = TextFactory.createSmallText("");
        TextDecorator.makeErrorText(errorMsg);
        VBox vbox = ContainerFactory.createVBoxElement(300);
        vbox.getChildren().add(menuTerrain);
        hBox.getChildren().addAll(errorMsg, vbox, btnFinish, btnStart);

    }

}