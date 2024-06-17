package org.copper.Admin.EditButton;

import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;
import org.copper.Questions.TextQuestion;

public class TextEdit extends EditPane {
    private final TextArea questionArea;
    private final TextArea solutionArea;

    public TextEdit() {
        super();
        this.questionArea = new TextArea();
        this.solutionArea = new TextArea();
        pane = new VBox(questionArea, solutionArea);
    }

    @Override
    public void load(Question question) {
        if(question instanceof TextQuestion){
            System.out.println(question);
            questionArea.setText(((TextQuestion) question).getQuestion());
            solutionArea.setText(((TextQuestion) question).getAnswer());
        }else {
            questionArea.clear();
            solutionArea.clear();
        }
    }

    @Override
    public void save(int[] index) {
        System.out.println("Save points: " + ApplicationContext.getPointMatrix()[index[0]][index[1]]);
        Questions.getQuestions()[index[0]][index[1]] = new TextQuestion(questionArea.getText(), solutionArea.getText(), ApplicationContext.getPointMatrix()[index[0]][index[1]]);
    }
}
