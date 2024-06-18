package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

public class TextQuestion extends Question {
    private String question;
    private String answer;

    @JsonCreator
    public TextQuestion(
            @JsonProperty("question") String question,
            @JsonProperty("answer") String answer,
            @JsonProperty("points") int points
    ) {
        super(ApplicationContext.QuestionTypes.TEXT, points);
        this.question = question;
        this.answer = answer;
    }

    @Override
    public void showQuestion() {
        Label question = new Label(getQuestion());
        StackPane pane = new StackPane(question);
        pane.getStyleClass().add("flowQuestion");
        PlayScreen.setChildRoot(pane);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "TextQuestion{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
