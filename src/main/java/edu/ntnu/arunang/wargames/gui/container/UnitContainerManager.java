package edu.ntnu.arunang.wargames.gui.container;

import edu.ntnu.arunang.wargames.model.Army;
import edu.ntnu.arunang.wargames.model.unit.Unit;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Manages multiple unit container by a given army. The cards are stored in a Flow-pane.
 * the cards can be shown in a condensed or normal view. condensed view removes health-points
 * and generalizes the units extensively. Condensed mode is used for simulation purposes.
 */

public class UnitContainerManager {
    private final HashMap<Unit, UnitContainer> containers;
    private final Army army;
    private final FlowPane flowPane;
    private final boolean condensed;

    /**
     * Constructs the object and creates a flowpane according to the given army.
     * Choose whether the object is condensed or normal.
     *
     * @param army      the unit that is viewed
     * @param condensed true or false
     */

    public UnitContainerManager(Army army, boolean condensed) {
        this.army = army;
        this.containers = new HashMap<>();
        this.flowPane = new FlowPane();
        this.condensed = condensed;
        initContainer();
    }


    /**
     * Initializes the container with the given start values.
     */

    private void initContainer() {
        for (Map.Entry<Unit, Integer> entry : new HashSet<>(getMap().entrySet())) {
            Unit unit = entry.getKey();
            UnitContainer container = new UnitContainer(unit, entry.getValue(), condensed);
            containers.put(unit, container);
            flowPane.getChildren().add(container.getGridPane());
        }
    }

    /**
     * Update the container. A new map gets parsed and the method searches for changes done.
     */

    public void updateContainer() {
        Map<Unit, Integer> units = getMap();

        for (Map.Entry<Unit, UnitContainer> entry : new HashSet<>(containers.entrySet())) {
            Unit unit = entry.getKey();

            //check if the unit from the army is in a container
            if (units.containsKey(unit)) {
                entry.getValue().updateUnit(units.get(unit));
                continue;
            }
            GridPane pane = entry.getValue().getGridPane();

            //hide the pane if it is not in the map
            if (pane.isVisible()) {
                pane.getChildren().clear();
                pane.getStyleClass().clear();
                pane.setVisible(false);
            }
        }
    }

    /**
     * Get the flowpane element
     *
     * @return flowpane element
     */

    public FlowPane getFlowpane() {
        return flowPane;
    }

    /**
     * Helper method for easily switching between condensed map and normal map.
     *
     * @return parsed map from an army.
     */

    private Map<Unit, Integer> getMap() {
        return condensed ? army.getCondensedMap() : army.getMap();
    }
}
