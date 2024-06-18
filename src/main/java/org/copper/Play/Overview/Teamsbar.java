package org.copper.Play.Overview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.PlayScreen;

import static org.copper.ApplicationContext.createSpacer;

public class Teamsbar {
    private final HBox root;

    private IntegerProperty points;

    public Teamsbar(){
        root = new HBox(createSpacer(true));
        PlayScreen.bindProperties(root.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 30 ,0 ,0);
        for (int i = 0; i < BuzzerServer.getBuzzers().size(); i++){
            root.getChildren().addAll(teamElement(i), createSpacer(true));
        }
    }

    public HBox getRoot() {
        return root;
    }

    private VBox teamElement(int number){
        Label teamName = new Label(BuzzerServer.getBuzzers().get(number).getTeamName());
        teamName.getStyleClass().add("teamName");
        Label points = new Label();
        points.textProperty().bind(Bindings.concat("Punkte: ", BuzzerServer.getBuzzers().get(number).getPoints().asString()));
        points.getStyleClass().add("points");
        return new VBox(teamName, points);
    }
}
