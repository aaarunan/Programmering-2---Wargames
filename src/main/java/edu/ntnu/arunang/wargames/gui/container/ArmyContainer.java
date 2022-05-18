package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.Army;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import static edu.ntnu.arunang.wargames.gui.factory.TextFactory.createSmallText;

/**
 * An object for viewing army stats. The armies are viewed in a gridpane. The reason for a
 * separate object is for easy and efficient update.
 */

public class ArmyContainer {

    private final Text armySize = createSmallText("");
    private final Text avgHealth = createSmallText("");
    private final Text avgArmor = createSmallText("");
    private final Text avgAttack = createSmallText("");
    private final int row = 0;
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

        gridPane.addRow(0, createSmallText("Count:"), armySize);
        gridPane.addRow(1, createSmallText("Total HP: "), avgHealth);
        gridPane.addRow(2, createSmallText("Total armor: "), avgArmor);
        gridPane.addRow(3, createSmallText("Total attack"), avgAttack);
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
     * Get the gridpane element.
     *
     * @return gridpane element
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
