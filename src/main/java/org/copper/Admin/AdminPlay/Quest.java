package org.copper.Admin.AdminPlay;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Buzzer.Team;
import org.copper.Play.PlayScreen;
import org.copper.Questions.Question;

import java.util.Optional;

public class Quest {
    VBox root;
    Question question;
    ObjectProperty<String> buzzeringTeam;
    public Quest() {
        this.root = new VBox();
        Label antwort = new Label();
        Button showSolution = new Button("Auflösen");
        root.getChildren().addAll(antwort, showSolution, new HBox());
        AdminPlayScene.getRoot().add(root , 0, 1);
    }

    private HBox pointsSection(){
        ComboBox<String> teams = new ComboBox<>(PlayScreen.getTeamNames());
        buzzeringTeam = teams.valueProperty();
        Label points = new Label(Integer.toString(question.getPoints()));
        Button correct = new Button("Korrekt");
        Button wrong = new Button("Falsch");
        correct.setOnAction((event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.getPoints().set(team.getPoints().getValue() + question.getPoints()));
            PlayScreen.goToOverview();
        }));
        wrong.setOnAction((event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> {
                team.getPoints().set(team.getPoints().getValue() - (question.getPoints() / ApplicationContext.getWrongMultiplier()));
                System.out.println(team.getPoints().get());
            });
            if(question.isBuzzer()){
                 BuzzerQueue.poll();
            }
        }));
        return new HBox(teams, points, correct, wrong);
    }

    public ObjectProperty<String> getBuzzeringTeamProperty() {
        return buzzeringTeam;
    }

    public void setQuestion(Question question) {
        this.question = question;
        root.getChildren().set(2, pointsSection());
    }

    public Question getQuestion() {
        return question;
    }
}
