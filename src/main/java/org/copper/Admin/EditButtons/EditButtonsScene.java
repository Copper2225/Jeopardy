package org.copper.Admin.EditButtons;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.copper.Play.PlayScreen;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;

import java.util.Arrays;

public class EditButtonsScene {
    private final VBox root;

    public EditButtonsScene() {
        EditButtonsGrid grid = new EditButtonsGrid();
        Button show = new Button("show");
        show.setOnAction((event -> {
            PlayScreen.start();
            Questions.save();
        }));
        root = new VBox(grid.gethBox(), show);
    }

    public VBox getRoot() {
        return root;
    }
}
