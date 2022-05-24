package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.gui.factory.TextFactory;
import edu.ntnu.arunang.wargames.model.army.Army;
import edu.ntnu.arunang.wargames.gui.decorator.TextDecorator;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * An object for viewing army stats. The armies are viewed in a grid-pane. The reason for a
 * separate object is for easy and efficient update.
 */

public class ArmyContainer {

    private final Label armySize = TextFactory.createSmallText("");
    private final Label avgHealth = TextFactory.createSmallText("");
    private final Label avgArmor = TextFactory.createSmallText("");
    private final Label avgAttack = TextFactory.createSmallText("");
    private Army army;
    private GridPane gridPane;

    public ArmyContainer(Army army) {
        this.army = army;
        initArmyContainer();
    }

    /**
     * Constructs the object and creates a gridpane according to the given army.
     */

    private void initArmyContainer() {
        gridPane = new GridPane();
        int i = 0;

        TextDecorator.setIcon(avgHealth, "health");
        TextDecorator.setIcon(avgArmor, "armor");
        TextDecorator.setIcon(avgAttack, "attack");

        gridPane.addRow(i++, TextFactory.createSmallText("Count:"), armySize);
        gridPane.addRow(i++, TextFactory.createSmallText("Total HP: "), avgHealth);
        gridPane.addRow(i++, TextFactory.createSmallText("Total armor: "), avgArmor);
        gridPane.addRow(i, TextFactory.createSmallText("Total attack"), avgAttack);
        updateData();

        // add the css
        gridPane.getStyleClass().add("grid-pane");

    }

    /**
     * Update the grid pane data according to the army stored.
     */

    public void updateData() {
        armySize.setText(Integer.toString(army.size()));
        avgHealth.setText(Integer.toString(army.getTotalHealthPoints()));
        avgArmor.setText(Integer.toString(army.getTotalAttackPoints()));
        avgAttack.setText(Integer.toString(army.getTotalArmorPoints()));
    }

    /**
     * Get the grid-pane element.
     *
     * @return grid-pane element
     */

    public GridPane getGridPane() {
        return this.gridPane;
    }

    /**
     * Set the army. Used when a new army is viewed.
     *
     * @param army the new army stored.
     */

    public void setArmy(Army army) {
        this.army = army;
    }
}
