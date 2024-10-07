package org.copper.Play.Overview;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;
import org.copper.Saver.CategoriesSaver;

public class OverviewSzene {
    private VBox root;

    GridPane grid = new GridPane();

    public OverviewSzene() {
        HBox categories = new HBox();
        categories.getStyleClass().add("categories");
        boolean[][] disabledMatrix = CategoriesSaver.loadCategories();
        for(int i = 0; i < ApplicationContext.getColumns(); i++){
            Label label = new Label(ApplicationContext.getCategories()[i]);
            categories.getChildren().add(label);
            label.getStyleClass().add("category");
            PlayScreen.bindProperties(label.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30, ApplicationContext.getColumns(), 30);
            for (int j = 0; j < ApplicationContext.getRows(); j++){
                Button bt = new Button(Integer.toString(ApplicationContext.getPointMatrix()[i][j]));
                bt.getStyleClass().add("myButton");
                bt.setDisable(disabledMatrix[i][j]);
                bt.prefHeightProperty().bind(grid.heightProperty().subtract(40*2).subtract((ApplicationContext.getRows() - 1) * 30).divide(ApplicationContext.getRows()));
                PlayScreen.bindProperties(bt.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30, ApplicationContext.getColumns(), 30);
                grid.add(bt, i, j);
            }
        }
        VBox.setVgrow(categories, Priority.SOMETIMES);
        VBox.setVgrow(grid, Priority.ALWAYS);
        grid.getStyleClass().add("overview");
        root = new VBox(categories, grid);
        root.setAlignment(Pos.CENTER);
    }

    public void toggleButtonDisabled(int c, int r, boolean alwaysDisable) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == r &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == c) {
                node.setDisable(!node.isDisabled() || alwaysDisable);
            }
        }
        CategoriesSaver.saveCategories(grid);
    }

    public VBox getRoot() {
        return root;
    }
}
