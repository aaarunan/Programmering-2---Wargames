package edu.ntnu.arunang.wargames.gui.factory;

import edu.ntnu.arunang.wargames.model.Army;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Factory for creating charts, and chart elements.
 */

public class ChartFactory {

    /**
     * Static class, should not be instantiated.
     */

    private ChartFactory() {

    }

    /**
     * Creates a line chart used to represent a battle. Shows the units of the attacking and defending army. Used when
     * simulating a battle.
     *
     * @param attacker attacking army
     * @param defender defending army
     * @return barchart element
     */

    public static LineChart<Number, Number> createLineChart(Army attacker, Army defender) {
        // create the axes of the barchart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Number of attacks");
        xAxis.setAutoRanging(true);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Units");
        yAxis.setAutoRanging(true);

        int size = defender.size();
        if (attacker.size() > defender.size()) {
            size = attacker.size();
        }
        yAxis.setUpperBound(size + 5);

        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setCreateSymbols(false);
        // can be turned off for performance
        lineChart.setAnimated(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);

        VBox.setVgrow(lineChart, Priority.ALWAYS);

        return lineChart;
    }

    /**
     * Creates a new Data series, with a name.
     *
     * @param name name of the data series
     * @return constructed data series
     */

    public static XYChart.Series<Number, Number> createDataSeries(String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        return series;
    }

    /**
     * Creates a data element. Requires a xIndex, which is the x-coordinates of the value. values is the y-coordinate of
     * the value.
     *
     * @param xIndex - x-coordinate
     * @param value  y-coordinate
     * @return Constructed data
     */

    public static XYChart.Data<Number, Number> createData(int xIndex, int value) {
        return new XYChart.Data<>(xIndex, value);
    }
}
