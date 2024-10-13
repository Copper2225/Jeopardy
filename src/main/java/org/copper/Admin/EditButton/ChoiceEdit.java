package org.copper.Admin.EditButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Questions.ChoiceQuestion;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;

import java.util.Arrays;

public class ChoiceEdit extends EditPane {

    private final ObservableList<String> names = FXCollections.observableArrayList();
    private final TextArea questionArea;
    private Label correctAnswer;
    public ChoiceEdit() {
        super();
        questionArea = new TextArea();
        pane = new VBox(typeChoose, questionArea, generateOptions());
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
    public void loadSpecific(Question question) {
        if (question instanceof ChoiceQuestion cQ) {
            names.setAll(cQ.getSolutions());
            questionArea.setText(cQ.getQuestion());
            correctAnswer.setText(cQ.getSolutions()[cQ.getCorrectIndex()]);
        }
    }

    @Override
    public void save(int[] index){
        Questions.getQuestions()[index[0]][index[1]] = new ChoiceQuestion(questionArea.getText(), names.indexOf(correctAnswer.getText()), names.toArray(String[]::new), Arrays.asList(BuzzerQueue.getBuzzerStates()).indexOf(typeChoose.getValue()), ApplicationContext.getPointMatrix()[index[0]][index[1]]);
    }
}
