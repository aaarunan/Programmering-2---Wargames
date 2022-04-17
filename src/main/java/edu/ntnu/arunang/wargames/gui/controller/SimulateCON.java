package edu.ntnu.arunang.wargames.gui.controller;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.Battle;
import edu.ntnu.arunang.wargames.gui.ArmySingleton;
import edu.ntnu.arunang.wargames.gui.GUI;
import edu.ntnu.arunang.wargames.gui.GUIFactory;
import edu.ntnu.arunang.wargames.observer.HitObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private VBox armyContainer;
    private Thread thread;
    private Army attacker = armySingleton.getAttacker().copy();
    private Army defender = armySingleton.getDefender().copy();
    private Battle battle = new Battle(attacker, defender);

    BarChart<String, Number> barChart = GUIFactory.createBarChart(attacker, defender);

    /**
     * Finishes the simulation. Redirects to the mainpage and clears the singleton.
     *
     * @param event
     */

    @FXML
    void onFinish(ActionEvent event) {
        armySingleton.clear();
        GUI.setSceneFromActionEvent(event, "main");
    }

    /**
     * Main process. Starts the simulation, and updates the gui accordingly.
     *
     * @throws InterruptedException
     */

    @FXML
    void onStart() throws InterruptedException {
        //logic for whether or not the button is restart or start
        if (btnStart.getText().equals("Start")) {
            btnStart.setText("Restart");
        } else if (btnStart.getText().equals("Restart")) {
            restart();
            btnStart.setText("Start");
            return;
        }

        HitObserver observer = new HitObserver(attacker, defender, battle, this);

        battle.attach(observer);
        Army winner = battle.simulate();
        battle.detach(observer);
        updateArmies(winner, battle.getLoser());
    }

    /**
     * Initializes the gui elements and creates a barchart
     */

    @FXML
    void initialize() {
        mainContainer.getChildren().add(barChart);
        updateArmies();
    }

    /**
     * Updates the barchart by getting the data from the armies.
     */

    public void updateBarChart() {
        //clears the barchart
        barChart.getData().clear();

        //creates the data again
        XYChart.Series<String, Number> attackerData = new XYChart.Series<>();
        attackerData.setName(attacker.getName());
        XYChart.Series<String, Number> defenderData = new XYChart.Series<>();
        defenderData.setName(defender.getName());

        //add the data to the barchart
        attackerData.getData().add(new XYChart.Data<>("Units", attacker.size()));
        defenderData.getData().add(new XYChart.Data<>("Units", defender.size()));

        barChart.getData().addAll(attackerData, defenderData);
        System.out.println("update");
    }

    /**
     * Update the gui with a winner and a loser army. Used when simulation is done.
     *
     * @param winner winning army
     * @param loser  losing army
     */

    void updateArmies(Army winner, Army loser) {
        //clear the container beforea adding
        armyContainer.getChildren().clear();

        armyContainer.getChildren().add(GUIFactory.createSmallTitle("Winner: " + winner.getName()));
        armyContainer.getChildren().add(GUIFactory.createArmyPane(winner));
        armyContainer.getChildren().add(GUIFactory.createSmallTitle("Loser: " + loser.getName()));
        armyContainer.getChildren().add(GUIFactory.createArmyPane(loser));
        armyContainer.getChildren().add(GUIFactory.createSmallText("Number of attacks: " + battle.getNumOfAttacks()));
    }

    /**
     * Update the army under simulation.
     */

    void updateArmies() {
        //clear the container before adding
        armyContainer.getChildren().clear();

        armyContainer.getChildren().add(GUIFactory.createSmallTitle(attacker.getName()));
        armyContainer.getChildren().add(GUIFactory.createArmyPane(attacker));
        armyContainer.getChildren().add(GUIFactory.createSmallTitle(defender.getName()));
        armyContainer.getChildren().add(GUIFactory.createArmyPane(defender));
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
        updateBarChart();
    }
}
