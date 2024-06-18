package org.copper.Admin.ConfigScene;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;


public class ConfigScene {
    private final VBox vBox;

    private static final String[] categories = new String[ApplicationContext.getColumns()];

    private static final ConfigGrid grid = new ConfigGrid();

    public ConfigScene() {
        ConfigMenu menu = new ConfigMenu();
        this.vBox = new VBox(menu.getMenu(), grid.gethBox());
        vBox.getStyleClass().add("spacedBox");
    }

    public static String[] getCategories() {
        return categories;
    }

    public static ConfigGrid getGrid() {
        return grid;
    }

    public VBox getRoot() {
        return vBox;
    }
}
