package org.copper.Play;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Buzzer.Team;
import org.copper.Play.Overview.OverviewSzene;
import org.copper.Play.Overview.Teamsbar;
import org.copper.Questions.Questions;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen {
    private static Stage playStage;
    private static OverviewSzene overview;
    private static VBox root;
    private static Teamsbar tB;
    private static ImageView logo;
    private static final List<Team> teams = new ArrayList<>();
    private static ObservableList<String> teamNames = FXCollections.observableArrayList();

    public static void start(){
        playStage = new Stage();
        Scene scene = new Scene(new Pane());
        scene.setFill(Paint.valueOf("black"));
        scene.getStylesheets().add("Play.css");
        playStage.setScene(scene);
        playStage.setAlwaysOnTop(true);
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.F11) playStage.setFullScreen(true);
        });
//        playStage.setX(-ApplicationContext.getScreenWidth());
//        playStage.setFullScreen(true);
        playStage.setWidth(ApplicationContext.getScreenWidth());
        playStage.setHeight(ApplicationContext.getScreenHeight());
        overview = new OverviewSzene();
        for(int i = 0; i < ApplicationContext.getTeamAmount(); i++){
            teams.add(new Team("Team " + (i + 1)));
            teamNames.add("Team " + (i + 1));
        }
        tB = new Teamsbar();
        logo = new ImageView(new Image("Kneipenquiz.png"));
        root = new VBox(logo, overview.getRoot(), tB.getRoot());
        VBox.setVgrow(logo, Priority.ALWAYS);
        VBox.setVgrow(tB.getRoot(), Priority.SOMETIMES);
        VBox.setVgrow(overview.getRoot(), Priority.ALWAYS);
        scene.setRoot(root);
        playStage.show();
    }

    public static void setChildRoot(Pane childRoot) {
        root.getChildren().set(1, childRoot);
        VBox.setVgrow(childRoot, Priority.ALWAYS);
    }

    public static void goToOverview(){
        if(root.getChildren().get(1) != overview.getRoot()){
            BuzzerQueue.setAllowBuzzer(false);
            AdminPlayScene.getInputs().showInputsProperty().set(false);
            int [] index = Questions.findQuestion(AdminPlayScene.getQuest().getQuestion());
            if(index.length == 2){
                PlayScreen.getOverview().toggleButtonDisabled(index[0], index[1], true);
            }
            BuzzerQueue.clear();
            root.getChildren().set(1, overview.getRoot());
        }

    }

    public static Stage getPlayStage() {
        return playStage;
    }

    public static void bindProperties(DoubleProperty property, ApplicationContext.Layouts layout, int padding, int elements, int spacing) {
        ReadOnlyDoubleProperty adminProp = switch (layout){
            case WITDH -> {
                yield playStage.widthProperty();

            }
            case HEIGHT -> {
                yield playStage.heightProperty();
            }
        };
        if(elements == 0){
            elements = 1;
        }
        property.bind(adminProp.subtract(padding*2).subtract((elements - 1) * spacing).divide(elements));
    }

    public static Teamsbar gettB() {
        return tB;
    }

    public static ImageView getLogo() {
        return logo;
    }

    public static OverviewSzene getOverview() {
        return overview;
    }

    public static VBox getRoot() {
        return root;
    }

    public static ObservableList<String> getTeamNames() {
        return teamNames;
    }

    public static List<Team> getTeams(){
        return teams;
    }


}
