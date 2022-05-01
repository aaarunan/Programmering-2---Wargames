package edu.ntnu.arunang.wargames.gui.factory;

import edu.ntnu.arunang.wargames.Army;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Factory for creating charts, and chart elements.
 */

public class ChartFactory {

    /**
     * Creates a line chart used to represent a battle. Shows the units of the
     * attacking and defending army. Used when simulating a battle.
     *
     * @param attacker attacking army
     * @param defender defending army
     * @return barchart element
     */

    public static LineChart<Number, Number> createBarChart(Army attacker, Army defender) {

        //create the axes of the barchart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Number of attacks");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Units");
        yAxis.autoRangingProperty().setValue(false);

        int size = defender.size();
        if (attacker.size() > defender.size()) {
            size = attacker.size();
        }
        yAxis.setUpperBound(size + 5);

        LineChart<Number, Number> barChart = new LineChart<Number, Number>(xAxis, yAxis);

        barChart.setCreateSymbols(false);
        //can be turned off for performance
        barChart.setAnimated(true);
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);

        return barChart;
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
     * Creates a data element. Requires a xIndex, which is the x-coordinates of the value.
     * values is the y-coordinate of the value.
     *
     * @param xIndex - x-coordinate
     * @param value y-coordinate
     * @return Constructed data
     */

    public static XYChart.Data<Number, Number> createData(int xIndex, int value) {
        return new XYChart.Data<>(xIndex, value);
    }
}
