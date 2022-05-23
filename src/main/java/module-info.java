module wargames {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;

    exports edu.ntnu.arunang.wargames.gui;
    exports edu.ntnu.arunang.wargames.gui.factory;
    exports edu.ntnu.arunang.wargames.model.unit;
    exports edu.ntnu.arunang.wargames.model.army;
    exports edu.ntnu.arunang.wargames.model.battle;
    exports edu.ntnu.arunang.wargames.event;

    opens edu.ntnu.arunang.wargames.gui.controller;
}