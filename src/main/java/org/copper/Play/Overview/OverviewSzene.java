package org.copper.Play.Overview;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

public class OverviewSzene {
    private VBox root;

    public OverviewSzene() {
        GridPane grid = new GridPane();
        grid.setOnKeyPressed(event -> {
            String keyName = event.getCode().toString();
            System.out.println("Key pressed: " + keyName);
        });
        for(int i = 0; i < ApplicationContext.getColumns(); i++){
            for (int j = 0; j < ApplicationContext.getRows(); j++){
                Button bt = new Button("Frage");
                PlayScreen.bindProperties(bt.prefHeightProperty(), ApplicationContext.Layouts.HEIGHT, 30, ApplicationContext.getRows(), 30);
                PlayScreen.bindProperties(bt.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30, ApplicationContext.getColumns(), 30);
                grid.add(bt, i, j);
            }
        }
        Teamsbar tB = new Teamsbar(4);
        root = new VBox(grid, tB.getRoot());
    }

    public VBox getRoot() {
        return root;
    }
}
