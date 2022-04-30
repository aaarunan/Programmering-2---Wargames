package edu.ntnu.arunang.wargames.gui.factory;

import edu.ntnu.arunang.wargames.Army;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ChartFactory {
    /**
     * Creates a barchart used to represent a battle. Shows the units of the
     * attacking and defending army. Used when simulating a battle.
     *
     * @param attacker attacking army
     * @param defender defending army
     * @return barchart element
     */

    public static LineChart<Number, Number> createBarChart(Army attacker, Army defender) {

        //create the axes of the barchart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Armyname");

        xAxis.setLabel("Army");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Units");
        yAxis.autoRangingProperty().setValue(false);

        int size = defender.size();
        if (attacker.size() > defender.size()) {
            size = attacker.size();
        }
        yAxis.setUpperBound(size+5);

        LineChart<Number, Number> barChart = new LineChart<Number, Number>(xAxis, yAxis);
        barChart.setTitle("Compare");

        //Create the data elements
        XYChart.Series<Number, Number> attackerData = new XYChart.Series<>();
        attackerData.setName(attacker.getName());
        XYChart.Series<Number, Number> defenderData = new XYChart.Series<>();
        defenderData.setName(defender.getName());

        //Add the data to the barchart
        attackerData.getData().add(new XYChart.Data<>(0, attacker.size()));
        defenderData.getData().add(new XYChart.Data<>(0, defender.size()));

        barChart.getData().addAll(attackerData, defenderData);

        barChart.setCreateSymbols(false);
        barChart.setAnimated(false);
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);

        return barChart;
    }
}
