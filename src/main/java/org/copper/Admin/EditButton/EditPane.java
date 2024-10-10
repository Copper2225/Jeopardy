package org.copper.Admin.EditButton;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Questions.Question;

abstract public class EditPane {
    protected Pane pane;
    protected ComboBox<String> typeChoose;

    public EditPane() {
        typeChoose = new ComboBox<>();
        typeChoose.getItems().addAll(BuzzerQueue.getBuzzerStates());
    }

    public Pane getPane() {
        return pane;
    }
    protected abstract void loadSpecific(Question question);

    public void load(Question question) {
        typeChoose.getSelectionModel().select(BuzzerQueue.getBuzzerStates()[question.getBuzzer()]);
        loadSpecific(question);
    };
    public abstract void save(int[] index);
}
