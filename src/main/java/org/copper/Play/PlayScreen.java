package org.copper.Play;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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
import org.copper.Saver.PointsSaver;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen {
    private static Stage playStage;
    private static OverviewSzene overview;
    private static VBox root;
    private static Teamsbar tB;
    private static ImageView logo = new ImageView();
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
        playStage.setX(ApplicationContext.getScreenWidth() * 2);
        playStage.setFullScreen(true);
        playStage.setWidth(ApplicationContext.getScreenWidth());
        playStage.setHeight(ApplicationContext.getScreenHeight());
        overview = new OverviewSzene();
        PointsSaver.loadPoints(teams, teamNames);
        tB = new Teamsbar();
        logo.setImage(new Image("Kneipenquiz.png"));
        root = new VBox();
        if(ApplicationContext.isShowLogo()) {
            root.getChildren().add(logo);
        }
        root.getChildren().addAll(overview.getRoot(), tB.getRoot());
        root.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(logo, Priority.ALWAYS);
        VBox.setVgrow(tB.getRoot(), Priority.SOMETIMES);
        VBox.setVgrow(overview.getRoot(), Priority.ALWAYS);
        scene.setRoot(root);
        logo.setPreserveRatio(true);
        logo.setFitHeight(logo.getImage().getHeight()/15*15);
        System.out.println(logo.getImage().getHeight());
        playStage.show();
    }

    public static void setChildRoot(Pane childRoot) {
        root.getChildren().set(ApplicationContext.isShowLogo() ? 1 : 0, childRoot);
//        logo.setVisible(childRoot.equals(overview.getRoot()) && ApplicationContext.isShowLogo());
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
            setChildRoot(overview.getRoot());
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

    public static Teamsbar getTb() {
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
