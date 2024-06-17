package org.copper.Play.Overview;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.PlayScreen;

public class Teamsbar {
    private final HBox root;

    public Teamsbar(){
        root = new HBox(createSpacer());
        PlayScreen.bindProperties(root.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30 ,0 ,0);
        for (int i = 0; i < BuzzerServer.getBuzzers().size(); i++){
            root.getChildren().addAll(teamElement(i), createSpacer());
        }
    }

    public HBox getRoot() {
        return root;
    }

    private VBox teamElement(int number){
        Label teamName = new Label(BuzzerServer.getBuzzers().get(number).getTeamName());
        teamName.getStyleClass().add("teamName");
        Label points = new Label("Punkte: " + 0);
        points.getStyleClass().add("points");
        return new VBox(teamName, points);
    }

    private Node createSpacer() {
        final Region spacer = new Region();
        // Make it always grow or shrink according to the available space
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinWidth(20);
        return spacer;
    }
}
