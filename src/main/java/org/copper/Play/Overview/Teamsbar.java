package org.copper.Play.Overview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.ApplicationContext;
import org.copper.Buzzer.Team;
import org.copper.Play.PlayScreen;

public class Teamsbar {
    private final HBox root;
    private Label buzzering;
    private final TeamTile[] teams = new TeamTile[ApplicationContext.getTeamAmount()];

    public Teamsbar(){
        root = new HBox();
        PlayScreen.bindProperties(root.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 0 ,0 ,0);
        root.getStyleClass().add("teamsbar");
        for (int i = 0; i < PlayScreen.getTeams().size(); i++){
            TeamTile teamTile = new TeamTile(i, root.heightProperty());
            teams[i] = teamTile;
            root.getChildren().add(teamTile.getRoot());
        }
    }

    public HBox getRoot() {
        return root;
    }

    public void buzzer(Team team){
        if(buzzering != null) {
            buzzering.getStyleClass().remove("buzzered");
        }
        int index = PlayScreen.getTeams().indexOf(team);
        buzzering = teams[index].getBuzzerIndicate();
        buzzering.getStyleClass().add("buzzered");
    }

    public void clearBuzzer() {
        if(buzzering != null) {
            buzzering.getStyleClass().remove("buzzered");
        }
        buzzering = null;
    }

    public void switchTeamName(int index, String newName){
        teams[index].getTeamName().setText(newName);
    }
}
