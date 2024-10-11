package org.copper.Admin.AdminPlay;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Buzzer.Team;
import org.copper.Play.PlayScreen;
import org.copper.Questions.*;

import java.util.*;

public class Quest {
    FlowPane root;
    Question question;
    ObjectProperty<String> buzzeringTeam = new SimpleObjectProperty<>("");
    Label antwort = new Label();
    Button play = new Button();
    HBox mediaZone = mediaZone();
    Button toOverview = toOverview();
    BooleanProperty aufgedeckt = new SimpleBooleanProperty(false);
    ComboBox<Integer> points = new ComboBox<Integer>();
    public Quest() {
        root = new FlowPane();
        root.getStyleClass().add("play-flow-pane");
        root.getChildren().addAll(questionAndAnswer(), new HBox(), mediaZone);
        AdminPlayScene.getRoot().add(root , 0, 1);
    }

    private VBox questionAndAnswer(){
        Label title = new Label();
        title.textProperty().bind(Bindings.when(aufgedeckt).then("Lösung").otherwise("Frage"));
        title.getStyleClass().add("answerTitle");
        antwort.getStyleClass().add("answerText");
        antwort.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30));
        antwort.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(7).subtract(180/7).add(15));
        antwort.maxWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30));
        antwort.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(7).subtract(180/7).add(15));
        Button showSolution = new Button();
        showSolution.textProperty().bind(Bindings.when(aufgedeckt).then("Auflösen").otherwise("Stellen"));
        showSolution.setOnAction(event -> {
            if(aufgedeckt.get()){
                question.showSolution();
            }else{
                question.showQuestion();
                switch (question.getType()){
                    case ApplicationContext.QuestionTypes.TEXT -> antwort.setText(((TextQuestion) question).getAnswer());
                    case ApplicationContext.QuestionTypes.BILD -> antwort.setText(((BildQuestion) question).getAnswer());
                    case ApplicationContext.QuestionTypes.AUDIO -> antwort.setText(((AudioQuestion) question).getAnswer());
                    case ApplicationContext.QuestionTypes.CHOICE -> antwort.setText(((ChoiceQuestion) question).getAnswer());
                }
                aufgedeckt.set(true);
            }

        });
        bind(showSolution);
        bind(title);
        return new VBox(title, antwort, showSolution);
    }

    private VBox pointsSection(){
        ComboBox<String> teams = new ComboBox<>(PlayScreen.getTeamNames());
        buzzeringTeam = teams.valueProperty();
        buzzeringTeam.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(question instanceof AudioQuestion && newValue != null && !BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[2]).get()){
                    ((AudioQuestion) question).getMediaPlayer().pause();
                }
            }
        });
        Set<Integer> pointsSet = new HashSet<>();
        int[] defaultPointValues = ApplicationContext.getDefaultPointValues();
        Arrays.stream(defaultPointValues)
                .boxed()
                .forEach(pointsSet::add);
        Arrays.stream(ApplicationContext.getPointMatrix())
                .flatMapToInt(Arrays::stream)
                .boxed()
                .forEach(pointsSet::add);
        points.getItems().addAll(pointsSet.stream().sorted().toList());
        points.getStyleClass().add("questionPoints");
        Button listCorrect = new Button("☑ Übersicht");
        Button add = getAddButton(teams);
        Button wrong = new Button("Falsch");
        listCorrect.setOnAction((event -> {
            for(int i = 0; i < ApplicationContext.getTeamAmount(); i++){
                if(AdminPlayScene.getInputs().getInputs()[i].isSelected()){
                    PlayScreen.getTeams().get(i).pointsProperty().set(PlayScreen.getTeams().get(i).pointsProperty().getValue() + points.getValue());
                }
            }
            PlayScreen.goToOverview();
        }));
        wrong.setOnAction((event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> {
                team.pointsProperty().set(team.pointsProperty().getValue() - (points.getValue() / ApplicationContext.getWrongMultiplier()));
            });
            if(BuzzerQueue.isAllowBuzzer()){
                 BuzzerQueue.poll();
            } else {
                teams.getSelectionModel().select("");
            }
        }));
        bind(teams, points);
        bind(add, listCorrect, wrong);
        return new VBox(new HBox(teams, points), new HBox(wrong, listCorrect, add));
    }

    private Button getAddButton(ComboBox<String> teams) {
        Button add = new Button("Hinzufügen");
        add.setOnAction(event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.pointsProperty().set(team.pointsProperty().getValue() + points.getValue()));
            points.getSelectionModel().selectPrevious();
            if(BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[1]).get()){
                BuzzerQueue.poll();
            } else if (BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[2]).get()) {
                optionalTeam.ifPresent(team -> {
                    Optional<Team> newTeam = PlayScreen.getTeams().stream().filter(team1 -> {
                        try {
                            return Integer.valueOf(team1.buzzerPositionProperty().get())
                                    .equals(Integer.parseInt(team.buzzerPositionProperty().get()) + 1);
                        } catch (NumberFormatException numberFormatException) {
                            return false;
                        }
                    }).findFirst();

                    newTeam.ifPresentOrElse(
                            newOne -> teams.getSelectionModel().select(newOne.getTeamName()),
                            () -> teams.getSelectionModel().select("")
                    );
                });

            }else {
                teams.getSelectionModel().select("");
            }
        });
        return add;
    }

    public ObjectProperty<String> getBuzzeringTeamProperty() {
        return buzzeringTeam;
    }

    public void setPointsValue(int number) {
        points.getSelectionModel().select(Integer.valueOf(number));
    }

    public void setQuestion(Question question) {
        this.question = question;
        if(!aufgedeckt.get()){
            switch (question.getType()){
                case ApplicationContext.QuestionTypes.TEXT -> antwort.setText(((TextQuestion) question).getQuestion());
                case ApplicationContext.QuestionTypes.BILD -> antwort.setText(((BildQuestion) question).getQuestion());
                case ApplicationContext.QuestionTypes.AUDIO -> antwort.setText(((AudioQuestion) question).getQuestion());
                case ApplicationContext.QuestionTypes.CHOICE -> antwort.setText(((ChoiceQuestion) question).getQuestion());
            }
        }else{
            switch (question.getType()){
                case ApplicationContext.QuestionTypes.TEXT -> antwort.setText(((TextQuestion) question).getAnswer());
                case ApplicationContext.QuestionTypes.BILD -> antwort.setText(((BildQuestion) question).getAnswer());
                case ApplicationContext.QuestionTypes.AUDIO -> antwort.setText(((AudioQuestion) question).getAnswer());
                case ApplicationContext.QuestionTypes.CHOICE -> antwort.setText(((ChoiceQuestion) question).getAnswer());
            }
        }
        ApplicationContext.isChoiceQuestionProperty().set(question instanceof ChoiceQuestion);
        AdminPlayScene.getInputs().reset();
        PlayScreen.setChildRoot(new HBox());
        root.getChildren().set(2, Objects.equals(question.getType(), ApplicationContext.QuestionTypes.AUDIO) ? mediaZone : toOverview);
        root.getChildren().set(1, pointsSection());
    }

    private HBox mediaZone() {
        play.setOnAction(event -> {
            if(((AudioQuestion) question).getMediaPlayer().getStatus() == MediaPlayer.Status.PAUSED){
                ((AudioQuestion) question).getMediaPlayer().play();
            }else{
                ((AudioQuestion) question).getMediaPlayer().pause();
            }

        });
        Button begin = new Button("Von Beginn");
        begin.setOnAction(event -> {
            ((AudioQuestion) question).getMediaPlayer().seek(Duration.ZERO);
            ((AudioQuestion) question).getMediaPlayer().play();
        });
        Button toOverview = new Button("Zur Übersicht");
        toOverview.setOnAction((event -> {
            PlayScreen.goToOverview();
        }));
        bind(begin, toOverview, play);
        return new HBox(begin, toOverview, play);
    }

    public Button toOverview(){
        Button toOverview = new Button("Zur Übersicht");
        toOverview.setOnAction((event -> {
            PlayScreen.goToOverview();
        }));
        bind(toOverview);
        return toOverview;
    }

    public Question getQuestion() {
        return question;
    }

    private void bind(Control... nodes){
        Arrays.stream(nodes).forEach(n -> {
            n.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).subtract((nodes.length - 1) * 10).divide(nodes.length));
            n.maxWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).subtract((nodes.length - 1) * 10).divide(nodes.length));
            n.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract((6) * 10).divide(7));
            n.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract((6) * 10).divide(7));
        });
    }

    public Button getPlay() {
        return play;
    }
}
