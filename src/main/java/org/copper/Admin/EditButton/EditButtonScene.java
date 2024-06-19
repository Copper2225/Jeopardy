package org.copper.Admin.EditButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;
import org.copper.Questions.BildQuestion;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;
import org.copper.Questions.TextQuestion;

public class EditButtonScene {
    private final VBox root;
    private Question question;
    private int[] index;
    private final ComboBox<String> typeChoose;
    private EditPane editPane;

    interface Types {
        String TEXT = "Text";
        String BILD = "Bild";
        String AUDIO = "Audio";
    }

    public EditButtonScene() {
        typeChoose = new ComboBox<>();
        typeChoose.getItems().addAll(Types.TEXT, Types.BILD, Types.AUDIO);
        Button save = new Button("Speichern");
        save.setOnAction((event -> save()));
        Button cancel = new Button("Abbrechen");
        cancel.setOnAction((event -> AdminScreen.switchScene(ApplicationContext.AdminScenes.EDIT_BUTTONS)));
        HBox menu = new HBox(typeChoose, save, cancel);
        root = new VBox(menu, new Pane());
        typeChoose.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                switchType(t1);
            }
        });
    }

    public void loadConfig(){
        index = ApplicationContext.getCurrentIndex();
        question = Questions.getQuestions()[index[0]][index[1]];
        String type = switch (question != null ? question.getType() : null){
            case ApplicationContext.QuestionTypes.TEXT -> Types.TEXT;
            case ApplicationContext.QuestionTypes.BILD -> Types.BILD;
            case null, default -> Types.TEXT;
        };
        if(type.equals(typeChoose.getValue())) switchType(type);
        else typeChoose.valueProperty().setValue(type);
    }

    private void switchType(String type) {
        switch (type){
            case Types.TEXT -> editPane = new TextEdit();
            case Types.BILD -> editPane = new BildEdit();
            case Types.AUDIO -> editPane = new AudioEdit();
            case null -> editPane = new TextEdit();
            default -> System.out.println("default");
        }
        root.getChildren().set(1, editPane.getPane());
        if(question != null){
            editPane.load(question);
        }
    }

    private void save(){
        System.out.println("save");
        editPane.save(index);
        AdminScreen.switchScene(ApplicationContext.AdminScenes.EDIT_BUTTONS);
    }

    public VBox getRoot() {
        return root;
    }
}
