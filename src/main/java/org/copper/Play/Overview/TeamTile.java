package org.copper.Play.Overview;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Play.PlayScreen;

public class TeamTile {
    private VBox root;
    public static final DoubleProperty inputSize = new SimpleDoubleProperty(2);
    private Label buzzerIndicate;
    private Label teamName;
    public TeamTile(int number, ReadOnlyDoubleProperty teamsBarHeight){
        teamName = new Label(PlayScreen.getTeams().get(number).getTeamName());
        teamName.getStyleClass().add("teamName");
        Label points = new Label();
        buzzerIndicate = points;
        points.textProperty().bind(Bindings.concat("Punkte: ", PlayScreen.getTeams().get(number).pointsProperty().asString()));
        points.getStyleClass().add("points");
        Label input = new Label();
        input.textProperty().bind(
                Bindings.when(BuzzerQueue.currentBuzzerStatusProperty().isNotEqualTo(BuzzerQueue.getBuzzerStates()[2]).or(PlayScreen.getTeams().get(number).showProperty()))
                        .then(Bindings.valueAt(AdminPlayScene.getInputs().getInputTexts(), number))
                        .otherwise(PlayScreen.getTeams().get(number).buzzerPositionProperty())
        );
        input.minHeightProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().heightProperty().multiply(0.2)).otherwise(0));
        input.maxHeightProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().heightProperty().multiply(0.2)).otherwise(0));
        input.prefWidthProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().widthProperty().divide(ApplicationContext.getTeamAmount()).subtract(25)).otherwise(0));
        input.maxWidthProperty().bind(Bindings.when(AdminPlayScene.getInputs().showInputsProperty()).then(PlayScreen.getPlayStage().widthProperty().divide(ApplicationContext.getTeamAmount()).subtract(25)).otherwise(0));
        input.getStyleClass().add("inputShow");
        input.styleProperty().bind(Bindings.concat("-fx-font-size: ").concat(Bindings.when(input.textProperty().length().lessThanOrEqualTo(2)).then(inputSize.multiply(5)).otherwise(inputSize)).concat("em;"));
        input.visibleProperty().bind(AdminPlayScene.getInputs().showInputsProperty());
        root = new VBox(input, teamName, points);
        teamName.minHeightProperty().bind(teamsBarHeight
                .subtract(points.heightProperty()).subtract(input.heightProperty()));
        root.maxWidthProperty().bind(PlayScreen.getPlayStage().widthProperty().divide(ApplicationContext.getTeamAmount()).subtract(30));
        root.getStyleClass().add("team");
        HBox.setHgrow(root, Priority.ALWAYS);
    }

    public VBox getRoot() {
        return root;
    }

    public void setRoot(VBox root) {
        this.root = root;
    }

    public static void setInputSize(double inputSize) {
        TeamTile.inputSize.set(inputSize);
    }

    public Label getBuzzerIndicate() {
        return buzzerIndicate;
    }

    public void setBuzzerIndicate(Label buzzerIndicate) {
        this.buzzerIndicate = buzzerIndicate;
    }

    public Label getTeamName() {
        return teamName;
    }

    public void setTeamName(Label teamName) {
        this.teamName = teamName;
    }
}
