package org.copper.Admin.AdminPlay;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Buzzer.Team;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.PlayScreen;

import java.util.Optional;
import java.util.regex.Pattern;

public class Edit {
    private final GridPane gridPane = new GridPane();
    private String buttonMode;
    private Label buzzerQueue;

    public Edit() {
        AdminPlayScene.getRoot().add(new VBox(editPoints(), overViewEdit(), buzzerZone()), 1 , 0);
    }

    public GridPane getRoot() {
        return gridPane;
    }

    private HBox editPoints() {
        ComboBox<String> teams = new ComboBox<>();
        teams.getItems().add("");
        teams.getItems().addAll(BuzzerServer.getBuzzers().stream().map(Team::getTeamName).toList());
        Pattern validEditingState = Pattern.compile("-?\\d*");
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (validEditingState.matcher(newText).matches()) {
                return change;
            }
            return null;
        });
        TextField points = new TextField();
        points.setTextFormatter(textFormatter);
        Button add = new Button("Hinzufügen");
        add.setOnAction((event -> {
            Optional<Team> optionalTeam = BuzzerServer.getBuzzers().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.getPoints().set(team.getPoints().getValue() + Integer.parseInt(points.getText())));
        }));
        Button set = new Button("Setzen");
        set.setOnAction((event -> {
            Optional<Team> optionalTeam = BuzzerServer.getBuzzers().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.getPoints().set(Integer.parseInt(points.getText())));
        }));
        return new HBox(teams, points, add, set);
    }

    private ComboBox<String> selectButtonMode(){
        ComboBox<String> modes = new ComboBox<>();
        modes.getItems().addAll(buttonModes.QUESTION, buttonModes.SOLUTION, buttonModes.DISABLE);
        modes.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                buttonMode = newValue;
            }
        });
        return modes;
    }

    private HBox overViewEdit() {
        Button toOverview = new Button("Zur Übersicht");
        toOverview.setOnAction((event -> {
            PlayScreen.goToOverview();
        }));
        return new HBox(selectButtonMode(), toOverview);
    }

    private HBox buzzerZone(){
        buzzerQueue = new Label("");
        buzzerQueue.setMinWidth(200);
        Button next = new Button("Nächster");
        next.setOnAction(event -> BuzzerQueue.poll());
        Button clear = new Button("Leeren");
        clear.setOnAction(event -> {
            BuzzerQueue.clear();
            buzzerQueue.setText("");
        });
        return new HBox(buzzerQueue, next, clear);
    }

    public void addBuzzer(Team team){
        Platform.runLater(() -> buzzerQueue.setText((buzzerQueue.getText() + "\n" + team.getTeamName()).trim()));
    }

    public void removeBuzzer(){
        buzzerQueue.setText(buzzerQueue.getText().substring(buzzerQueue.getText().indexOf("\n")).trim());
    }

    public String getButtonMode() {
        return buttonMode;
    }
}
