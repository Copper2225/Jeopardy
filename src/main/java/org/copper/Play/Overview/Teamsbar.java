package org.copper.Play.Overview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
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

    private IntegerProperty points;
    private Label buzzering;
    private final Label[] buzzerIndicates = new Label[ApplicationContext.getTeamAmount()];
    private final Label[] teamNames = new Label[ApplicationContext.getTeamAmount()];
    private final DoubleProperty inputSize = new SimpleDoubleProperty(2);

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
        int index = PlayScreen.getTeams().indexOf(team);
        buzzering = buzzerIndicates[index];
        buzzering.getStyleClass().add("buzzered");
    }

    public void clearBuzzer() {
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
        points.textProperty().bind(Bindings.concat("Punkte: ", PlayScreen.getTeams().get(number).pointsProperty().asString()));
        points.getStyleClass().add("points");
        Label input = new Label();
        input.textProperty().bind(Bindings.valueAt(AdminPlayScene.getInputs().getInputTexts(), number));
        input.minHeightProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().heightProperty().multiply(0.2)).otherwise(0));
        input.maxHeightProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().heightProperty().multiply(0.2)).otherwise(0));
        input.prefWidthProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().widthProperty().divide(ApplicationContext.getTeamAmount()).subtract(25)).otherwise(0));
        input.maxWidthProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().widthProperty().divide(ApplicationContext.getTeamAmount()).subtract(25)).otherwise(0));
        input.getStyleClass().add("inputShow");
        input.styleProperty().bind(Bindings.concat("-fx-font-size: ").concat(Bindings.when(ApplicationContext.isChoiceQuestionProperty()).then(inputSize.multiply(5)).otherwise(inputSize)).concat("em;"));
        input.visibleProperty().bind(AdminPlayScene.getInputs().showInputsProperty());
        VBox team = new VBox(input, teamName, points);
        teamName.minHeightProperty().bind(root.heightProperty()
                .subtract(points.heightProperty()).subtract(input.heightProperty()));
        team.maxWidthProperty().bind(PlayScreen.getPlayStage().widthProperty().divide(ApplicationContext.getTeamAmount()).subtract(30));
        team.getStyleClass().add("team");
        HBox.setHgrow(team, Priority.ALWAYS);
        return team;
    }

    public double getInputSize() {
        return inputSize.get();
    }

    public DoubleProperty inputSizeProperty() {
        return inputSize;
    }

    public void switchTeamName(int index, String newName){
        teamNames[index].setText(newName);
    }
}
