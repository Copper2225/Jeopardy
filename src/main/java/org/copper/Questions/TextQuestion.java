package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

import static org.copper.ApplicationContext.createSpacer;

public class TextQuestion extends Question {
    private String question;
    private String answer;

    @JsonCreator
    public TextQuestion(
            @JsonProperty("question") String question,
            @JsonProperty("answer") String answer,
            @JsonProperty("points") int points,
            @JsonProperty("buzzer") boolean buzzer
    ) {
        super(ApplicationContext.QuestionTypes.TEXT, points, buzzer);
        this.question = question;
        this.answer = answer;
    }

    @Override
    public void showQuestion() {
        super.showQuestion();
        Label question = new Label(getQuestion());
        StackPane pane = new StackPane(question);
        pane.getStyleClass().addAll("stackQuestion", "textQuestion");
        PlayScreen.setChildRoot(pane);
    }

    @Override
    public void showSolution(){
        super.showSolution();
        Label question = new Label(getQuestion());
        question.getStyleClass().add("topLabel");
        Label solution = new Label(getAnswer());
        solution.getStyleClass().add("textQuestion");
        VBox vBox = new VBox(question, createSpacer(false), solution, createSpacer(false));
        question.prefWidthProperty().bind(vBox.widthProperty());
        vBox.getStyleClass().addAll("stackQuestion", "bildQuestion");
        PlayScreen.setChildRoot(vBox);
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
