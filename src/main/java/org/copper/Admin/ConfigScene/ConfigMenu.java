package org.copper.Admin.ConfigScene;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.copper.Admin.AdminScreen;
import org.copper.Admin.EditButtons.EditButtonsScene;
import org.copper.ApplicationContext;

public class ConfigMenu {
    private final HBox menu;

    public ConfigMenu() {
        menu = new HBox(save());
    }

    private Button save(){
        Button save = new Button("Speichern");
        save.setOnAction((event) -> {
            AdminScreen.setEditButtonsScene(new EditButtonsScene());
            AdminScreen.switchScene(ApplicationContext.AdminScenes.EDIT_BUTTONS);
        });
        return save;
    }

    public HBox getMenu() {
        return menu;
    }
}
