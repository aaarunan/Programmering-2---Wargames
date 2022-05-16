package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import static edu.ntnu.arunang.wargames.gui.factory.TextFactory.createSmallText;

public class UnitContainer {

    private Unit unit;
    private int count;

    private GridPane gridPane;
    private boolean condenced;

    private Text name = createSmallText("");
    private Text healthPoints = createSmallText("");
    private Text armorPoints = createSmallText("");
    private Text attackPoints = createSmallText("");
    private Text txtCount = createSmallText("");
    private Text unitType = createSmallText("");

    public UnitContainer(Unit unit, int count, boolean condenced) {
        this.unit = unit;
        this.count = count;
        this.condenced = condenced;
        makeUnitContainer();
    }

    private void makeUnitContainer() {
        gridPane = new GridPane();

        gridPane.addRow(0, createSmallText("Type:"), unitType);
        gridPane.addRow(1, createSmallText("Name:"), name);
        gridPane.addRow(2, createSmallText("Armorpoints:"), armorPoints);
        gridPane.addRow(3, createSmallText("Attackpoints:"), attackPoints);
        gridPane.addRow(4, createSmallText("Count:"), txtCount);
        if (!condenced) {
            gridPane.addRow(5, createSmallText("Healthpoints:"), healthPoints);
        }

        name.setText(unit.getName());
        healthPoints.setText(Integer.toString(unit.getHealthPoints()));
        armorPoints.setText(Integer.toString(unit.getArmorPoints()));
        attackPoints.setText(Integer.toString(unit.getAttackPoints()));
        unitType.setText(unit.getClass().getSimpleName());

        updateData();

        //add the css
        gridPane.getStyleClass().add("grid-pane");

    }

    public void updateUnit(int count) {
        this.count = count;
        updateData();
    }

    private void updateData() {
        txtCount.setText(Integer.toString(count));
    }


    public GridPane getGridPane() {
        return this.gridPane;
    }
}

