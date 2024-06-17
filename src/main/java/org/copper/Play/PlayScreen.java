package org.copper.Play;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerServer;
import org.copper.Play.Overview.OverviewSzene;

public class PlayScreen {
    private static Stage playStage;
    private static OverviewSzene overview;

    public static void start(){
        playStage = new Stage();
        Scene scene = new Scene(new Pane());
        scene.getStylesheets().add("Play.css");
        playStage.setScene(scene);
        playStage.setX(-ApplicationContext.getScreenWidth());
        playStage.setWidth(ApplicationContext.getScreenWidth());
        playStage.setHeight(ApplicationContext.getScreenHeight());
        overview = new OverviewSzene();
        scene.setRoot(overview.getRoot());
        BuzzerServer.initialize();
        playStage.show();
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
        property.bind(adminProp.subtract(padding*2).subtract((elements - 1) * spacing).divide(elements));
    }
}
