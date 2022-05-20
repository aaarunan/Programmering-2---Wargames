package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;


import java.util.Locale;

import static edu.ntnu.arunang.wargames.gui.factory.TextFactory.createSmallText;

/**
 * Container that is used to make a gui element to view units. The stats of the units
 * is put in a gridpane. The reason for a separate object is for easy and efficient updating
 * when simulation occurs.
 * <p>
 * There are to separate ways to display an unit, condensed and normal.
 * condensed does not show the healthpoints. This is for simulation purposes.
 */

class UnitContainer {
    private final Unit unit;
    private final boolean condensed;
    private final Label name = createSmallText("");
    private final Label healthPoints = createSmallText("");
    private final Label armorPoints = createSmallText("");
    private final Label attackPoints = createSmallText("");
    private final Label txtCount = createSmallText("");
    private final Label unitType = createSmallText("");
    private int count;
    private GridPane gridPane;

    /**
     * Constructs the object and creates a gridpane according to the given unit and count.
     * Choose whether the gridpane is condensed or normal.
     *
     * @param unit      the unit that is viewed
     * @param count     sum of units
     * @param condensed true or false
     */

    public UnitContainer(Unit unit, int count, boolean condensed) {
        this.unit = unit;
        this.count = count;
        this.condensed = condensed;
        initUnitContainer();
    }

    /**
     * Initializes the gridpane. Assigns initial values to the grid.
     */

    private void initUnitContainer() {
        int i = 0;
        gridPane = new GridPane();

        TextDecorator.setIcon(healthPoints, "health");
        TextDecorator.setIcon(armorPoints, "armor");
        TextDecorator.setIcon(attackPoints, "attack");
        TextDecorator.setIcon(unitType, unit.getClass().getSimpleName().toLowerCase(Locale.ROOT));

        //add all fields
        gridPane.addRow(i++, createSmallText("Name:"), name);
        gridPane.addRow(i++, createSmallText("Type:"), unitType);
        gridPane.addRow(i++, createSmallText("Armorpoints:"), armorPoints);
        gridPane.addRow(i++, createSmallText("Attackpoints:"), attackPoints);
        if (!condensed) {
            gridPane.addRow(i++, createSmallText("Healthpoints:"), healthPoints);
        }
        gridPane.addRow(i, createSmallText("Count:"), txtCount);

        //update the values according to the unit
        name.setText(unit.getName());
        healthPoints.setText(Integer.toString(unit.getHealthPoints()));
        armorPoints.setText(Integer.toString(unit.getArmorPoints()));
        attackPoints.setText(Integer.toString(unit.getAttackPoints()));
        unitType.setText(unit.getClass().getSimpleName());

        updateData();

        // add the css
        gridPane.getStyleClass().add("grid-pane");

    }

    /**
     * Update the gridpane with a given count.
     *
     * @param newCount new count
     */

    public void updateUnit(int newCount) {
        this.count = newCount;
        updateData();
    }

    /**
     * update the data on the grid pane.
     */

    private void updateData() {
        txtCount.setText(Integer.toString(count));
    }

    /**
     * Get the gridpane element.
     *
     * @return constructed gridpane
     */

    public GridPane getGridPane() {
        return this.gridPane;
    }
}
