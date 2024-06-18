package org.copper.Play.Overview;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

import static org.copper.Buzzer.BuzzerServer.stop;

public class OverviewSzene {
    private VBox root;

    public OverviewSzene() {
        ImageView logo = new ImageView(new Image("Kneipenquiz.png"));
        GridPane grid = new GridPane();
        HBox categories = new HBox();
        categories.getStyleClass().add("categories");
        for(int i = 0; i < ApplicationContext.getColumns(); i++){
            Label label = new Label(ApplicationContext.getCategories()[i]);
            categories.getChildren().add(label);
            label.getStyleClass().add("category");
            PlayScreen.bindProperties(label.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30, ApplicationContext.getColumns(), 30);
            for (int j = 0; j < ApplicationContext.getRows(); j++){
                Button bt = new Button(Integer.toString(ApplicationContext.getPointMatrix()[i][j]));
                bt.getStyleClass().add("myButton");
                bt.prefHeightProperty().bind(grid.heightProperty().subtract(30*2).subtract((ApplicationContext.getRows() - 1) * 30).divide(ApplicationContext.getRows()));
                PlayScreen.bindProperties(bt.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30, ApplicationContext.getColumns(), 30);
                grid.add(bt, i, j);
            }
        }
        Teamsbar tB = new Teamsbar();
        VBox.setVgrow(tB.getRoot(), Priority.ALWAYS);
        VBox.setVgrow(categories, Priority.ALWAYS);
        VBox.setVgrow(grid, Priority.ALWAYS);
        root = new VBox(logo, categories, grid, tB.getRoot());
    }

    public VBox getRoot() {
        return root;
    }
}
