package org.copper.Admin.ConfigScene;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.copper.ApplicationContext;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ConfigGrid {
    private final HBox hBox;

    private final TextField[] categoryFields = new TextField[ApplicationContext.getColumns()];
    public ConfigGrid() {
        hBox = new HBox();
        hBox.getStyleClass().add("spacedBox");
        for(int i = 0; i < ApplicationContext.getColumns(); i++) {
            TextField category = new TextField();
            categoryFields[i] = category;
            VBox vBox = new VBox(category);
            vBox.getStyleClass().add("spacedBox");
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
                Question question = Questions.getQuestions()[i][j];
                int initialValue = question != null ? question.getPoints() : (j + 1) * 100;
                spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, initialValue, 50));
                vBox.getChildren().add(spinner);
            }
            hBox.getChildren().add(vBox);
        }
        load();
    }

    private void load(){
        ObjectMapper mapper = new ObjectMapper();
        File fCategories = new File("src/main/resources/" + "questions/categories" +".json");
        try {
            ApplicationContext.setCategories(mapper.readValue(fCategories, new TypeReference<>() {}));
            for (int i = 0; i < ApplicationContext.getCategories().length; i++){
                categoryFields[i].setText(ApplicationContext.getCategories()[i]);
            }

        } catch (MismatchedInputException ignored){
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public TextField[] getCategoryFields() {
        return categoryFields;
    }

    public HBox gethBox() {
        return hBox;
    }
}
