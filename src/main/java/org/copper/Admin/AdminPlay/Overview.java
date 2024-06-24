package org.copper.Admin.AdminPlay;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;
import org.copper.Questions.Questions;

public class Overview {

    public Overview() {
        HBox categories = new HBox();
        for( String cat : ApplicationContext.getCategories()){
            Label l = new Label(cat);
            l.setStyle("-fx-alignment: center;");
            l.prefWidthProperty().bind(categories.widthProperty().divide(ApplicationContext.getCategories().length));
            categories.getChildren().add(l);
        }
        GridPane grid = new GridPane();
        grid.getStyleClass().add("buttonGrid");
        for(int i = 0; i < ApplicationContext.getColumns(); i++){
            for(int j = 0; j < ApplicationContext.getRows(); j++){
                Button bt = new Button(Integer.toString(ApplicationContext.getPointMatrix()[i][j]));
                bt.prefHeightProperty().bind(grid.heightProperty().subtract((ApplicationContext.getRows() - 1) * 10).divide(ApplicationContext.getRows()));
                bt.prefWidthProperty().bind(grid.widthProperty().subtract((ApplicationContext.getColumns() - 1) * 10).divide(ApplicationContext.getColumns()));
                int finalI = i;
                int finalJ = j;
                bt.setOnAction((event -> {
                    switch (AdminPlayScene.getEdit().getButtonMode()){
                        case buttonModes.QUESTION -> Questions.getQuestions()[finalI][finalJ].showQuestion();
                        case buttonModes.DISABLE -> PlayScreen.getOverview().toggleButtonDisabled(finalI, finalJ, false);
                        case buttonModes.SOLUTION -> Questions.getQuestions()[finalI][finalJ].showSolution();
                    }
                }));
                GridPane.setHalignment(bt, HPos.CENTER);
                grid.add(bt, i, j);
            }
        }
        VBox play = new VBox(categories, grid);
        play.prefWidthProperty().bind(AdminPlayScene.getRoot().widthProperty().divide(2));
        play.prefHeightProperty().bind(AdminPlayScene.getRoot().heightProperty().divide(2));
        play.maxHeightProperty().bind(AdminPlayScene.getRoot().heightProperty().divide(2).subtract(30));
        play.maxWidthProperty().bind(AdminPlayScene.getRoot().widthProperty().divide(2).subtract(30));
        VBox.setVgrow(grid, Priority.ALWAYS);
        categories.prefWidthProperty().bind(play.widthProperty());
        AdminPlayScene.getRoot().add(play , 0, 0);
    }
}
