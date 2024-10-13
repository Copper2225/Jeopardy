package org.copper.Admin.EditButton;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;
import org.copper.Questions.TextQuestion;

import java.util.Arrays;

public class TextEdit extends EditPane {
    private final TextArea questionArea;
    private final TextArea solutionArea;

    public TextEdit() {
        super();
        this.questionArea = new TextArea();
        this.solutionArea = new TextArea();
        pane = new VBox(typeChoose, questionArea, solutionArea);
    }

    @Override
    public void loadSpecific(Question question) {
        if(question instanceof TextQuestion tQ){
            questionArea.setText(tQ.getQuestion());
            solutionArea.setText(tQ.getAnswer());
        }else {
            questionArea.clear();
            solutionArea.clear();
        }
    }

    @Override
    public void save(int[] index) {
        Questions.getQuestions()[index[0]][index[1]] = new TextQuestion(questionArea.getText(), solutionArea.getText(), ApplicationContext.getPointMatrix()[index[0]][index[1]], Arrays.asList(BuzzerQueue.getBuzzerStates()).indexOf(typeChoose.getValue()));
    }
}
