package org.copper.Play.Overview;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Teamsbar {
    HBox root;

    public Teamsbar(int teamsAmount){
        root = new HBox();
        for (int i = 1; i <= teamsAmount; i++){
            root.getChildren().add(teamElement(i));
        }
    }

    public HBox getRoot() {
        return root;
    }

    private VBox teamElement(int number){
        Label teamName = new Label("Team "+ number);
        Label points = new Label("Punkte: " + 0);
        return new VBox(teamName, points);
    }
}
