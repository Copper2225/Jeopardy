package org.copper.Admin.ConfigScene;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


public class ConfigScene {
    private final VBox vBox;

    public ConfigScene() {
        ConfigGrid grid = new ConfigGrid();
        ConfigMenu menu = new ConfigMenu();
        this.vBox = new VBox(menu.getMenu(), grid.gethBox());
    }

    public VBox getRoot() {
        return vBox;
    }
}
