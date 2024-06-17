package org.copper.Play.Overview;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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
        for(int i = 0; i < ApplicationContext.getColumns(); i++){
            Label label = new Label(ApplicationContext.getCategories()[i]);
            label.getStyleClass().add("category");
            GridPane.setHalignment(label, HPos.CENTER);
            grid.add(label, i, 0);
            for (int j = 1; j <= ApplicationContext.getRows(); j++){
                Button bt = new Button("Frage");
                bt.getStyleClass().add("myButton");
//                bt.setDisable(true);
                bt.prefHeightProperty().bind(grid.heightProperty().subtract(ApplicationContext.getRows()*10).divide(ApplicationContext.getRows()+1));
                PlayScreen.bindProperties(bt.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30, ApplicationContext.getColumns(), 30);
                grid.add(bt, i, j);
            }
        }
        Teamsbar tB = new Teamsbar();
        VBox.setVgrow(tB.getRoot(), Priority.ALWAYS);
        VBox.setVgrow(grid, Priority.ALWAYS);
        root = new VBox(logo, grid, tB.getRoot());
    }

    public VBox getRoot() {
        return root;
    }
}
