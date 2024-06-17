package org.copper;

import javafx.application.Application;
import javafx.stage.Stage;
import org.copper.Admin.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage admin) throws Exception {
        ApplicationContext.loadApplicationContext();
        AdminScreen.setAdminStage(admin);
        AdminScreen.loadAdmin();
        AdminScreen.switchScene(ApplicationContext.AdminScenes.CONFIG);
    }
}