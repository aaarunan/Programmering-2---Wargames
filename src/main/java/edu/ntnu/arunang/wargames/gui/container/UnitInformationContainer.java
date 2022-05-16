package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.Army;
import edu.ntnu.arunang.wargames.unit.Unit;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class UnitInformationContainer {
    private HashMap<Unit, UnitContainer> containers;
    private Army army;
    private FlowPane flowPane;
    private boolean condenced;

    public UnitInformationContainer(Army army, boolean condenced) {
        this.army = army;
        this.containers = new HashMap<>();
        this.flowPane = new FlowPane();
        this.condenced = condenced;
        makeContainer();
    }

    private void makeContainer() {
        for (Map.Entry<Unit, Integer> entry : getMap().entrySet()) {
            Unit unit = entry.getKey();
            UnitContainer container = new UnitContainer(unit, entry.getValue(), condenced);
            containers.put(unit, container);
            flowPane.getChildren().add(container.getGridPane());
        }
    }

    public void updateContainer() {
        Map<Unit, Integer> units = getMap();

        for (Map.Entry<Unit, UnitContainer> entry : containers.entrySet()) {
            Unit unit = entry.getKey();

            if (units.containsKey(unit)) {
                entry.getValue().updateUnit(units.get(unit));
                continue;
            }
            GridPane pane = entry.getValue().getGridPane();

            if (pane.isVisible()) {
                pane.getChildren().clear();
                pane.getStyleClass().clear();
                pane.setVisible(false);
            }
        }
    }

    public FlowPane getFlowpane() {
        return flowPane;
    }

    private Map<Unit, Integer> getMap() {
        return condenced ? army.getCondensedMap() : army.getMap();
    }
}
