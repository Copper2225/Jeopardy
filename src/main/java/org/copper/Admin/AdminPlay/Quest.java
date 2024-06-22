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
import org.copper.Questions.BildQuestion;
import org.copper.Questions.Question;
import org.copper.Questions.TextQuestion;

import java.util.Optional;

public class Quest {
    VBox root;
    Question question;
    ObjectProperty<String> buzzeringTeam;
    Label antwort;
    public Quest() {
        this.root = new VBox();
        antwort = new Label();
        root.getChildren().addAll(antwort, new HBox());
        AdminPlayScene.getRoot().add(root , 0, 1);
    }

    private HBox pointsSection(){
        Button showSolution = new Button("Auflösen");
        showSolution.setOnAction(event -> {
            question.showSolution();
        });
        ComboBox<String> teams = new ComboBox<>(PlayScreen.getTeamNames());
        buzzeringTeam = teams.valueProperty();
        Label points = new Label(Integer.toString(question.getPoints()));
        Button correct = new Button("Korrekt");
        Button add = new Button("Hinzufügen");
        add.setOnAction(event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.getPoints().set(team.getPoints().getValue() + question.getPoints()));
        });
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
        return new HBox(showSolution, teams, points, add, correct, wrong);
    }

    public ObjectProperty<String> getBuzzeringTeamProperty() {
        return buzzeringTeam;
    }

    public void setQuestion(Question question) {
        this.question = question;
        switch (question.getType()){
            case ApplicationContext.QuestionTypes.TEXT -> antwort.setText(((TextQuestion) question).getAnswer());
            case ApplicationContext.QuestionTypes.BILD -> antwort.setText(((BildQuestion) question).getAnswer());
        }
        root.getChildren().set(1, pointsSection());
    }

    public Question getQuestion() {
        return question;
    }
}
