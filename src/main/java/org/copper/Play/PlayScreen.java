package org.copper.Play;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.Overview.OverviewSzene;
import org.copper.Play.Overview.Teamsbar;

public class PlayScreen {
    private static Stage playStage;
    private static OverviewSzene overview;
    private static VBox root;
    private static Teamsbar tB;
    private static ImageView logo;

    public static void start(){
        playStage = new Stage();
        Scene scene = new Scene(new Pane());
        scene.getStylesheets().add("Play.css");
        playStage.setScene(scene);
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.F11) playStage.setFullScreen(true);
        });
        playStage.setX(-ApplicationContext.getScreenWidth());
        playStage.setFullScreen(true);
        playStage.setWidth(ApplicationContext.getScreenWidth());
        playStage.setHeight(ApplicationContext.getScreenHeight());
        overview = new OverviewSzene();
        tB = new Teamsbar();
        logo = new ImageView(new Image("Kneipenquiz.png"));
        root = new VBox(logo, overview.getRoot(), tB.getRoot());
        VBox.setVgrow(overview.getRoot(), Priority.ALWAYS);
        scene.setRoot(root);
        playStage.show();
    }

    public static void setChildRoot(Pane childRoot) {
        root.getChildren().set(1, childRoot);
        VBox.setVgrow(childRoot, Priority.ALWAYS);
    }

    public static void goToOverview(){
        root.getChildren().set(1, overview.getRoot());
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
}
