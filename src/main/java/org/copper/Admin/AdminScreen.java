package org.copper.Admin;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.Admin.ConfigScene.ConfigScene;
import org.copper.Admin.EditButton.EditButtonScene;
import org.copper.Admin.EditButtons.EditButtonsScene;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;
import org.copper.Questions.Questions;

import static org.copper.Buzzer.BuzzerServer.stop;

public class AdminScreen {
    private static Stage adminStage;
    private static Scene adminScene;
    private static ConfigScene configScene;
    private static EditButtonsScene editButtonsScene;
    private static EditButtonScene editButtonScene;
    private static AdminPlayScene adminPlayScene;

    public static void loadAdmin(){
        Questions.load();
        configScene = new ConfigScene();
        editButtonScene = new EditButtonScene();
        adminScene = new Scene(new Pane());
        adminScene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.F11) adminStage.setFullScreen(true);
        });
        adminStage.setScene(adminScene);
        adminStage.setX(ApplicationContext.getScreenWidth() + 10);
        adminStage.setWidth(ApplicationContext.getScreenWidth());
        adminStage.setHeight(ApplicationContext.getScreenHeight());
//        adminStage.setMaximized(true);
        adminStage.setAlwaysOnTop(true);
        adminStage.getScene().getStylesheets().add("Admin.css");
        adminStage.setOnCloseRequest((event) -> {
            stop();
            if( PlayScreen.getPlayStage() != null ){
                PlayScreen.getPlayStage().close();
            }

        });
        adminStage.show();
    }

    public static EditButtonsScene getEditButtonsScene() {
        return editButtonsScene;
    }

    public static void setEditButtonsScene(EditButtonsScene editButtonsScene) {
        AdminScreen.editButtonsScene = editButtonsScene;
    }

    public static void switchScene(ApplicationContext.AdminScenes scene){
        switch (scene) {
            case CONFIG -> adminScene.setRoot(configScene.getRoot());
            case EDIT_BUTTONS -> adminScene.setRoot(editButtonsScene.getRoot());
            case EDIT_BUTTON -> {
                editButtonScene.loadConfig();
                adminScene.setRoot(editButtonScene.getRoot());
            }
            case ADMIN_OVERVIEW -> adminScene.setRoot(AdminPlayScene.getRoot());
        }
    }

    public static Stage getAdminStage() {
        return adminStage;
    }

    public static void setAdminStage(Stage adminStage) {
        AdminScreen.adminStage = adminStage;
    }

    public static void setAdminPlayScene(AdminPlayScene adminPlayScene) {
        AdminScreen.adminPlayScene = adminPlayScene;
    }
}
