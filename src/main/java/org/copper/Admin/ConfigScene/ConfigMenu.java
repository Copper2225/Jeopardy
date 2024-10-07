package org.copper.Admin.ConfigScene;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.copper.Admin.AdminScreen;
import org.copper.Admin.EditButtons.EditButtonsScene;
import org.copper.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
            ApplicationContext.setCategories(Arrays.stream(ConfigScene.getGrid().getCategoryFields()).map(TextField::getText).toArray(String[]::new));
            ObjectMapper mapper = new ObjectMapper();
            File fCategories = new File("src/main/resources/" + "questions/categories" +".json");
            try {
                mapper.writerWithDefaultPrettyPrinter().writeValue(fCategories, ApplicationContext.getCategories());
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        return save;
    }

    public HBox getMenu() {
        return menu;
    }
}
