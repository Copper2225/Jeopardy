package org.copper.Admin.EditButtons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;

import java.util.Arrays;

public class EditButtonsGrid {
    private final HBox hBox;

    public EditButtonsGrid() {
        hBox = new HBox();
        hBox.getStyleClass().add("grid");
        for(int i = 0; i < ApplicationContext.getColumns(); i++) {
            Label category = new Label(ApplicationContext.getCategories()[i]);
            VBox vBox = new VBox(category);
            vBox.setAlignment(Pos.CENTER);
            for (int j = 0; j < ApplicationContext.getRows(); j++) {
                Button button = new Button(Integer.toString(ApplicationContext.getPointMatrix()[i][j]));
                final int column = i;
                final int row = j;
                button.setOnAction((event) -> editButton(column, row));
                ApplicationContext.bindProperties(button.minWidthProperty(), ApplicationContext.Layouts.WITDH, 20, ApplicationContext.getColumns(), 50);
                vBox.getChildren().add(button);
            }
            hBox.getChildren().add(vBox);
        }
    }

    private void editButton(int column, int row) {
        System.out.println(column + " + " + row);
        ApplicationContext.setCurrentIndex(new int[]{column, row});
        AdminScreen.switchScene(ApplicationContext.AdminScenes.EDIT_BUTTON);
    }

    public HBox gethBox() {
        return hBox;
    }
}
