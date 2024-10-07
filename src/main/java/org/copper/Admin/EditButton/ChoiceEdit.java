package org.copper.Admin.EditButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Questions.ChoiceQuestion;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;

public class ChoiceEdit extends EditPane {

    private final ObservableList<String> names = FXCollections.observableArrayList();
    private final TextArea questionArea;
    private Label correctAnswer;
    public ChoiceEdit() {
        super();
        questionArea = new TextArea();
        pane = new VBox(questionArea, generateOptions());
    }

    private FlowPane generateOptions(){
        ListView<String> list = new ListView<>(names);
        TextField optionName = new TextField();
        Button add = new Button("+");
        Button correct = new Button("Korrekt");
        correctAnswer = new Label();
        correct.setOnAction(event -> {
            correctAnswer.setText(list.getSelectionModel().getSelectedItem());
        });
        Button delete = new Button("-");
        delete.setOnAction(event -> {
            names.removeAll(list.getSelectionModel().getSelectedItems());
        });
        add.setOnAction(event -> {
            names.add(optionName.getText());
        });
        list.setEditable(true);
        return new FlowPane(list, optionName, add, delete, correct, correctAnswer);
    }

    @Override
    public void load(Question question) {
        if (question instanceof ChoiceQuestion) {
            names.setAll(((ChoiceQuestion) question).getSolutions());
            questionArea.setText(((ChoiceQuestion) question).getQuestion());
            correctAnswer.setText(((ChoiceQuestion) question).getSolutions()[((ChoiceQuestion) question).getCorrectIndex()]);
        }
    }

    @Override
    public void save(int[] index){
        Questions.getQuestions()[index[0]][index[1]] = new ChoiceQuestion(questionArea.getText(), names.indexOf(correctAnswer.getText()), names.toArray(String[]::new), ApplicationContext.getPointMatrix()[index[0]][index[1]]);
    }
}
