package org.copper.Play.Overview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Buzzer.Team;
import org.copper.Play.PlayScreen;

import static org.copper.ApplicationContext.createSpacer;

public class Teamsbar {
    private final HBox root;

    private IntegerProperty points;
    private Label buzzering;
    private final Label[] buzzerIndicates = new Label[ApplicationContext.getTeamAmount()];
    private final Label[] teamNames = new Label[ApplicationContext.getTeamAmount()];

    public Teamsbar(){
        root = new HBox();
        PlayScreen.bindProperties(root.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 0 ,0 ,0);
        root.getStyleClass().add("teamsbar");
        for (int i = 0; i < PlayScreen.getTeams().size(); i++){
            root.getChildren().addAll(teamElement(i));
        }
    }

    public HBox getRoot() {
        return root;
    }

    public void buzzer(Team team){
        if(buzzering != null) {
            buzzering.getStyleClass().remove("buzzered");
        }
        int index = BuzzerServer.getBuzzers().indexOf(team);
        buzzering = buzzerIndicates[index];
        buzzering.getStyleClass().add("buzzered");
    }

    public void clearBuzzer() {
        System.out.println(buzzering.getText());
        if(buzzering != null) {
            buzzering.getStyleClass().remove("buzzered");
        }
        buzzering = null;
    }

    private VBox teamElement(int number){
        Label teamName = new Label(PlayScreen.getTeams().get(number).getTeamName());
        teamNames[number] = teamName;
        teamName.getStyleClass().add("teamName");
        Label points = new Label();
        buzzerIndicates[number] = points;
        points.textProperty().bind(Bindings.concat("Punkte: ", PlayScreen.getTeams().get(number).getPoints().asString()));
        points.getStyleClass().add("points");
        VBox team = new VBox(teamName, points);
        team.maxWidthProperty().bind(PlayScreen.getPlayStage().widthProperty().divide(ApplicationContext.getTeamAmount()).subtract(30));
        team.getStyleClass().add("team");
        HBox.setHgrow(team, Priority.ALWAYS);
        return team;
    }

    public void switchTeamName(int index, String newName){
        teamNames[index].setText(newName);
    }
}
