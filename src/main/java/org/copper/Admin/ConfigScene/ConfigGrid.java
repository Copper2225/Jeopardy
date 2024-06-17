package org.copper.Admin.ConfigScene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;

public class ConfigGrid {
    private final HBox hBox;
    public ConfigGrid() {
        hBox = new HBox();
        for(int i = 0; i < ApplicationContext.getColumns(); i++) {
            TextField category = new TextField();
            VBox vBox = new VBox(category);
            vBox.setAlignment(Pos.CENTER);
            ApplicationContext.bindProperties(vBox.prefWidthProperty(), ApplicationContext.Layouts.WITDH, 20, ApplicationContext.getColumns(), 50);
            for (int j = 0; j < ApplicationContext.getRows(); j++) {
                Spinner<Integer> spinner = new Spinner<>();
                int finalI = i;
                int finalJ = j;
                spinner.valueProperty().addListener(new ChangeListener<Integer>() {
                    @Override
                    public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                        ApplicationContext.getPointMatrix()[finalI][finalJ] = t1;
                    }
                });
                spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, (j + 1) * 100, 50));
                vBox.getChildren().add(spinner);
            }
            hBox.getChildren().add(vBox);
        }
    }

    public HBox gethBox() {
        return hBox;
    }
}
