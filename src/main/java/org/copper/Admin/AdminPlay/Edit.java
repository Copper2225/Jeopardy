package org.copper.Admin.AdminPlay;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.copper.Admin.AdminScreen;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Buzzer.Team;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.PlayScreen;

import java.util.Arrays;
import java.util.Optional;

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
            optionalTeam.ifPresent(team -> team.pointsProperty().set(team.pointsProperty().getValue() + Integer.parseInt(points.getText())));
        }));
        Button rename = getRename(teams, points);
        Button set = new Button("Setzen");
        set.setOnAction((event -> {
            Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> team.getTeamName().equals(teams.getValue()))).findFirst();
            optionalTeam.ifPresent(team -> team.pointsProperty().set(Integer.parseInt(points.getText())));
        }));
        bind(teams, points);
        bind(add, rename,  set);
        return new FlowPane(new HBox(teams, points), new HBox(add, rename, set));
    }

    private static Button getRename(ComboBox<String> teams, TextField points) {
        Button rename = new Button("Umbennen");
        rename.setOnAction((event -> {
            int oldIndex = teams.getSelectionModel().getSelectedIndex();
            PlayScreen.getTb().switchTeamName(oldIndex, points.getText());
            PlayScreen.getTeams().get(oldIndex).setTeamName(points.getText());
            Optional<Team> buzzer = BuzzerServer.getTeams().stream().filter(team -> team.getTeamName().equals(points.getText())).findFirst();
            buzzer.ifPresent(buzz -> PlayScreen.getTeams().get(oldIndex).setiPAddress(buzz.getiPAddress()));
            PlayScreen.getTeamNames().set(oldIndex, points.getText());

        }));
        return rename;
    }

    private ComboBox<String> selectButtonMode(){
        ComboBox<String> modes = new ComboBox<>();
        modes.getItems().addAll(buttonModes.QUESTION, buttonModes.LOAD, buttonModes.SOLUTION, buttonModes.DISABLE);
        modes.setValue(buttonModes.QUESTION);
        modes.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                buttonMode = newValue;
            }
        });
        return modes;
    }

    private FlowPane buzzerZone(){
        buzzerQueue = new Label();
        buzzerQueue.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().subtract(70).divide(3));
        buzzerQueue.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().multiply(3).divide(14).subtract(270/7).add(20));
        buzzerQueue.maxWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().subtract(70).divide(3));
        buzzerQueue.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().multiply(3).divide(14).subtract(270/7).add(20));
        buzzerQueue.getStyleClass().add("buzzerQueue");
        Button next = new Button("Nächster");
        next.setOnAction(event -> BuzzerQueue.poll());
        Button clear = new Button("Leeren");
        clear.setOnAction(event -> {
            BuzzerQueue.clear();
            buzzerQueue.setText("");
        });
        Button allow = new Button();
        allow.textProperty().bind(BuzzerQueue.currentBuzzerStatusProperty());
        allow.styleProperty().bind(
                Bindings.when(BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[0]))
                        .then("-fx-background-color: lightcoral;")
                        .otherwise(
                                Bindings.when(BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[1]))
                                        .then("-fx-background-color: lightgreen;")
                                        .otherwise("-fx-background-color: khaki;")
                        )
        );
        allow.setOnAction(event -> {
            BuzzerQueue.toggleStatus();
        });
        bind(next, clear, allow);
        VBox controls = new VBox(next, clear, allow);
        return new FlowPane(new HBox(controls, buzzerQueue));
    }

    private FlowPane teamsEdit(){
        ComboBox<String> buzzerNames = new ComboBox<>(BuzzerServer.getBuzzerNames());
        ComboBox<String> displayNames = new ComboBox<>(PlayScreen.getTeamNames());
        Button link = new Button("Überschreiben");
        link.setOnAction(event -> {
            int oldIndex = displayNames.getSelectionModel().getSelectedIndex();
            PlayScreen.getTb().switchTeamName(oldIndex, buzzerNames.getValue());
            PlayScreen.getTeams().get(oldIndex).setTeamName(buzzerNames.getValue());
            Optional<Team> buzzer = BuzzerServer.getTeams().stream().filter(team -> team.getTeamName().equals(buzzerNames.getValue())).findFirst();
            buzzer.ifPresent(buzz -> PlayScreen.getTeams().get(oldIndex).setiPAddress(buzz.getiPAddress()));
            PlayScreen.getTeamNames().set(oldIndex, buzzerNames.getValue());
        });
        bind(buzzerNames, displayNames, link);
        ComboBox<String> selectionMode = selectButtonMode();
        Button blackOut = new Button("BLACK");
        bind(blackOut, selectionMode);
        blackOut.setOnAction(event -> PlayScreen.getPlayStage().getScene().getRoot().setVisible(!PlayScreen.getPlayStage().getScene().getRoot().isVisible()));
        return new FlowPane(new HBox(displayNames, buzzerNames, link), new HBox(selectionMode, blackOut));
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

    private void bind(Control... nodes){
        Arrays.stream(nodes).forEach(n -> {
            n.prefWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).subtract((nodes.length - 1) * 10).divide(nodes.length));
            n.prefHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(30).subtract((6) * 10).divide(7));
            n.maxWidthProperty().bind(AdminScreen.getAdminStage().widthProperty().divide(2).subtract(30).subtract((nodes.length - 1) * 10).divide(nodes.length));
            n.maxHeightProperty().bind(AdminScreen.getAdminStage().getScene().heightProperty().divide(2).subtract(30).subtract((6) * 10).divide(7));
        });
    }
}
