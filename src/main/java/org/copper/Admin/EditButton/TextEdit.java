package org.copper.Admin.EditButton;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;
import org.copper.Questions.TextQuestion;

public class TextEdit extends EditPane {
    private final TextArea questionArea;
    private final TextArea solutionArea;
    private final CheckBox buzzer;

    public TextEdit() {
        super();
        this.questionArea = new TextArea();
        this.solutionArea = new TextArea();
        this.buzzer = new CheckBox("Buzzerfrage");
        pane = new VBox(buzzer, questionArea, solutionArea);
    }

    @Override
    public void load(Question question) {
        if(question instanceof TextQuestion){
            questionArea.setText(((TextQuestion) question).getQuestion());
            solutionArea.setText(((TextQuestion) question).getAnswer());
            buzzer.setSelected(question.isBuzzer());
        }else {
            questionArea.clear();
            solutionArea.clear();
        }
    }

    @Override
    public void save(int[] index) {
        System.out.println("Save points: " + ApplicationContext.getPointMatrix()[index[0]][index[1]]);
        Questions.getQuestions()[index[0]][index[1]] = new TextQuestion(questionArea.getText(), solutionArea.getText(), ApplicationContext.getPointMatrix()[index[0]][index[1]], buzzer.isSelected());
    }
}
