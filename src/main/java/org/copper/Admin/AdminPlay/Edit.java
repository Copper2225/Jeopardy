package org.copper.Admin.AdminPlay;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.Buzzer.Team;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.PlayScreen;

import java.util.Optional;
import java.util.regex.Pattern;

public class Edit {
    private final GridPane gridPane = new GridPane();
    private String buttonMode;

    public Edit() {
        AdminPlayScene.getRoot().add(new VBox(editPoints(), overViewEdit()), 1 , 0);
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

    public String getButtonMode() {
        return buttonMode;
    }
}
