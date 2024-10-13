package org.copper.Admin.EditButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Questions.ListQuestion;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;
import org.copper.Questions.TextQuestion;

import java.util.Arrays;

public class ListEdit extends EditPane {
    private final ObservableList<String> hints = FXCollections.observableArrayList();
    private final TextArea questionArea;
    private final TextArea solutionArea;
    private final CheckBox hintRank;

    public ListEdit() {
        super();
        this.questionArea = new TextArea();
        this.solutionArea = new TextArea();
        this.hintRank = new CheckBox("Hint ranking");
        pane = new VBox(typeChoose, hintRank, questionArea, solutionArea, generateOptions());
        hintRank.visibleProperty().bind(typeChoose.valueProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[2]));
    }

    private FlowPane generateOptions() {
        ListView<String> list = new ListView<>(hints);
        TextField optionName = new TextField();
        Button add = new Button("+");
        Button up = new Button("↑");
        Button down = new Button("↓");
        Button delete = new Button("-");
        up.setOnAction(event -> {
            int selectedIndex = list.getSelectionModel().getSelectedIndex();
            if (selectedIndex > 0) {
                String selectedItem = hints.get(selectedIndex);
                hints.set(selectedIndex, hints.get(selectedIndex - 1));
                hints.set(selectedIndex - 1, selectedItem);
                list.getSelectionModel().select(selectedIndex - 1);
            }
        });
        down.setOnAction(event -> {
            int selectedIndex = list.getSelectionModel().getSelectedIndex();
            if (selectedIndex < hints.size() - 1 && selectedIndex >= 0) {
                String selectedItem = hints.get(selectedIndex);
                hints.set(selectedIndex, hints.get(selectedIndex + 1));
                hints.set(selectedIndex + 1, selectedItem);
                list.getSelectionModel().select(selectedIndex + 1);
            }
        });
        delete.setOnAction(event -> {
            hints.removeAll(list.getSelectionModel().getSelectedItems());
        });
        add.setOnAction(event -> {
            hints.add(optionName.getText());
        });
        list.setEditable(true);
        return new FlowPane(list, optionName, add, delete, up, down);
    }

    @Override
    public void loadSpecific(Question question) {
        if(question instanceof ListQuestion lQ){
            questionArea.setText(lQ.getQuestion());
            solutionArea.setText(lQ.getAnswer());
            hints.addAll(lQ.getHints());
            hintRank.setSelected(lQ.isHintRank());
        }
    }

    @Override
    public void save(int[] index) {
        Questions.getQuestions()[index[0]][index[1]] = new ListQuestion(solutionArea.getText(), questionArea.getText(), hints.toArray(new String[0]), hintRank.isSelected() && typeChoose.getValue().equals(BuzzerQueue.getBuzzerStates()[2]), Arrays.asList(BuzzerQueue.getBuzzerStates()).indexOf(typeChoose.getValue()), ApplicationContext.getPointMatrix()[index[0]][index[1]]);
    }
}
