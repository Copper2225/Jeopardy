package org.copper.Admin.EditButtons;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.copper.Admin.AdminPlay.Overview;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;
import org.copper.Questions.Questions;

public class EditButtonsScene {
    private final VBox root;

    public EditButtonsScene() {
        EditButtonsGrid grid = new EditButtonsGrid();
        Button show = new Button("show");
        show.setOnAction((event -> {
            PlayScreen.start();
            AdminScreen.setOverviewSzene(new Overview());
            AdminScreen.switchScene(ApplicationContext.AdminScenes.ADMIN_OVERVIEW);
            Questions.save();
        }));
        root = new VBox(grid.gethBox(), show);
        root.getStyleClass().add("spacedBox");
    }

    public VBox getRoot() {
        return root;
    }
}
