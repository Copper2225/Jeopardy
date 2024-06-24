package org.copper.Admin.AdminPlay;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.copper.Admin.AdminScreen;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Buzzer.Team;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.PlayScreen;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public class Edit {
    private String buttonMode = buttonModes.QUESTION;
    private Label buzzerQueue;
    private FlowPane root = new FlowPane();

    public Edit() {
        root.getChildren().addAll(teamsEdit(), editPoints(), buzzerZone());
        root.getStyleClass().add("play-flow-pane");
        AdminPlayScene.getRoot().add(root, 1, 0);
    }

    private FlowPane editPoints() {
        ComboBox<String> teams = new ComboBox<>(PlayScreen.getTeamNames());
        TextField points = new TextField();
        Button add = new Button("Verändern");
        add.setOnAction((event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.getPoints().set(team.getPoints().getValue() + Integer.parseInt(points.getText())));
        }));
        Button rename = getRename(teams, points);
        Button set = new Button("Setzen");
        set.setOnAction((event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.getPoints().set(Integer.parseInt(points.getText())));
        }));
        bind(2, teams, points);
        bind(3, add, rename,  set);
        return new FlowPane(new HBox(teams, points), new HBox(add, rename, set));
    }

    private static Button getRename(ComboBox<String> teams, TextField points) {
        Button rename = new Button("Umbennen");
        rename.setOnAction((event -> {
            int oldIndex = teams.getSelectionModel().getSelectedIndex();
            Optional <Team> buzzer = PlayScreen.getTeams().stream().filter(team -> team.getTeamName().equals(teams.getValue())).findAny();
            buzzer.ifPresent(team -> {
                PlayScreen.gettB().switchTeamName(oldIndex, points.getText());
                team.setTeamName(points.getText());
                team.setiPAddress(BuzzerServer.getBuzzers().get(PlayScreen.getTeams().indexOf(team)).getiPAddress());
                PlayScreen.getTeamNames().set(oldIndex, points.getText());
                BuzzerServer.getTeams().get(PlayScreen.getTeams().indexOf(team)).setTeamName(points.getText());
            });

        }));
        return rename;
    }

    private ComboBox<String> selectButtonMode(){
        ComboBox<String> modes = new ComboBox<>();
        modes.getItems().addAll(buttonModes.QUESTION, buttonModes.SOLUTION, buttonModes.DISABLE);
        modes.setValue(buttonModes.QUESTION);
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

    private FlowPane buzzerZone(){
        buzzerQueue = new Label();
        buzzerQueue.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().subtract(70).divide(3));
        buzzerQueue.prefHeightProperty().bind(AdminScreen.getAdminStage().heightProperty().multiply(3).subtract(280).divide(14));
        buzzerQueue.maxWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().subtract(70).divide(3));
        buzzerQueue.maxHeightProperty().bind(AdminScreen.getAdminStage().heightProperty().multiply(3).subtract(280).divide(14));
        buzzerQueue.getStyleClass().add("buzzerQueue");
        Button next = new Button("Nächster");
        next.setOnAction(event -> BuzzerQueue.poll());
        Button clear = new Button("Leeren");
        clear.setOnAction(event -> {
            BuzzerQueue.clear();
            buzzerQueue.setText("");
        });
        Button allow = new Button("Freigeben");
        allow.styleProperty().bind(Bindings.when(BuzzerQueue.allowBuzzerProperty()).then("-fx-background-color: lightgreen;").otherwise("-fx-background-color: lightcoral;"));
        allow.setOnAction(event -> {
            BuzzerQueue.setAllowBuzzer(!BuzzerQueue.isAllowBuzzer());
        });
        bind(3, next, clear, allow);
        VBox controls = new VBox(next, clear, allow);
        return new FlowPane(new HBox(controls, buzzerQueue));
    }

    private FlowPane teamsEdit(){
        ComboBox<String> buzzerNames = new ComboBox<>(BuzzerServer.getBuzzerNames());
        ComboBox<String> displayNames = new ComboBox<>(PlayScreen.getTeamNames());
        Button link = new Button("Überschreiben");
        link.setOnAction(event -> {
            int oldIndex = displayNames.getSelectionModel().getSelectedIndex();
            int newIndex = buzzerNames.getSelectionModel().getSelectedIndex();
            PlayScreen.gettB().switchTeamName(oldIndex, buzzerNames.getValue());
            PlayScreen.getTeams().get(newIndex).setTeamName(buzzerNames.getValue());
            PlayScreen.getTeams().get(newIndex).setiPAddress(BuzzerServer.getBuzzers().get(newIndex).getiPAddress());
            PlayScreen.getTeamNames().set(oldIndex, buzzerNames.getValue());
        });
        bind(2, buzzerNames, displayNames);
        ComboBox<String> selectionMode = selectButtonMode();
        bind(2, link, selectionMode);
        return new FlowPane(new HBox(displayNames, buzzerNames), new HBox(selectionMode, link));
    }

    public void addBuzzer(Team team){
        Platform.runLater(() -> buzzerQueue.setText((buzzerQueue.getText() + "\n" + team.getTeamName()).trim()));
    }

    public void removeBuzzer(){
        int index = buzzerQueue.getText().indexOf("\n");
        if (index > 0){
            buzzerQueue.setText(buzzerQueue.getText().substring(buzzerQueue.getText().indexOf("\n")).trim());
        }
        else buzzerQueue.setText("");
    }

    public String getButtonMode() {
        return buttonMode;
    }

    private void bind(int elements, Control... nodes){
        Arrays.stream(nodes).forEach(n -> {
            n.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().subtract(80).divide(2).subtract((elements - 1) * 10).divide(elements));
            n.prefHeightProperty().bind(AdminScreen.getAdminStage().heightProperty().subtract(80).divide(2).subtract((6) * 10).divide(7));
            n.maxWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().subtract(80).divide(2).subtract((elements - 1) * 10).divide(elements));
            n.maxHeightProperty().bind(AdminScreen.getAdminStage().heightProperty().subtract(80).divide(2).subtract((6) * 10).divide(7));
        });
    }
}
