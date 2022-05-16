package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.Army;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import static edu.ntnu.arunang.wargames.gui.factory.TextFactory.createSmallText;

public class ArmyContainer {

    private Army army;

    private GridPane gridPane;

    private Text armySize = createSmallText("");
    private Text avgHealth =  createSmallText("");
    private Text avgArmor =  createSmallText("");
    private Text avgAttack =  createSmallText("");

    private int row = 0;

    public ArmyContainer(Army army) {
        this.army = army;
        makeArmyContainer();
    }

    private GridPane makeArmyContainer() {
        gridPane = new GridPane();

        gridPane.addRow(0, createSmallText("Count:"), armySize);
        gridPane.addRow(1, createSmallText("Total HP: "), avgHealth);
        gridPane.addRow(2, createSmallText("Total armor: "), avgArmor);
        gridPane.addRow(3, createSmallText("Total attack"), avgAttack);
        updateData();

        //add the css
        gridPane.getStyleClass().add("grid-pane");

        return gridPane;
    }

    public void updateData() {
        armySize.setText(Integer.toString(army.size()));
        avgHealth.setText(Integer.toString(army.getTotalHealthPoints()));
        avgArmor.setText(Integer.toString(army.getTotalAttackPoints()));
        avgAttack.setText(Integer.toString(army.getTotalArmorPoints()));
    }

    public GridPane getGridPane() {
        return this.gridPane;
    }

    public void setArmy(Army army) {
        this.army = army;
    }
}
