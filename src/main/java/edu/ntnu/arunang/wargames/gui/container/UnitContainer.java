package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

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
    private final Text name = createSmallText("");
    private final Text healthPoints = createSmallText("");
    private final Text armorPoints = createSmallText("");
    private final Text attackPoints = createSmallText("");
    private final Text txtCount = createSmallText("");
    private final Text unitType = createSmallText("");
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
        gridPane = new GridPane();

        //add all fields
        gridPane.addRow(0, createSmallText("Type:"), unitType);
        gridPane.addRow(1, createSmallText("Name:"), name);
        gridPane.addRow(2, createSmallText("Armorpoints:"), armorPoints);
        gridPane.addRow(3, createSmallText("Attackpoints:"), attackPoints);
        gridPane.addRow(4, createSmallText("Count:"), txtCount);
        if (!condensed) {
            gridPane.addRow(5, createSmallText("Healthpoints:"), healthPoints);
        }

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
