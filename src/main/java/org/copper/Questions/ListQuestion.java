package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

import java.util.Arrays;

import static org.copper.ApplicationContext.createSpacer;

public class ListQuestion extends Question {
    @JsonIgnore
    private Label[] hintLabels;
    private String answer;
    private String question;
    private String[] hints;
    private boolean hintRank;

    @JsonCreator
    public ListQuestion(
            @JsonProperty("answer") String answer,
            @JsonProperty("question") String question,
            @JsonProperty("hints") String[] hints,
            @JsonProperty("hintRank") boolean hintRank,
            @JsonProperty("buzzer") int buzzer,
            @JsonProperty("points") int points
    ) {
        super(ApplicationContext.QuestionTypes.LIST, points, buzzer);
        this.question = question;
        this.answer = answer;
        this.hints = hints;
        this.hintRank = hintRank;
        this.hintLabels = new Label[hints.length];
    }

    @Override
    public void showQuestion() {
        super.showQuestion();
        Label question = new Label(getQuestion());
        question.getStyleClass().add("topLabel");
        VBox hintsBox = new VBox();
        VBox questRoot = new VBox(question, hintsBox);
        for(int i = 0 ; i < hints.length; i++) {
            Label text = new Label((i + 1) + ". " + hints[i]);
            text.prefHeightProperty().bind(questRoot.heightProperty().subtract(question.heightProperty()).divide(hints.length/2).subtract(30));
            text.getStyleClass().add("listHint");
            text.setVisible(false);
            hintLabels[i] = text;
            hintsBox.getChildren().add(text);
        }
        questRoot.getStyleClass().addAll("stackQuestion");
        question.prefWidthProperty().bind(questRoot.widthProperty());
        PlayScreen.setChildRoot(questRoot);
    }

    @Override
    public void showSolution() {
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

    public void next() {
        for (int i = 0; i < hintLabels.length; i++) {
            if(!hintLabels[i].isVisible()) {
                if( i == hintLabels.length -1){
                    AdminPlayScene.getQuest().getAntwort().setText("Antwort: " + answer);
                } else {
                    AdminPlayScene.getQuest().getAntwort().setText("Antwort: " + answer + "\n" + "Next: " + hints[i+1]);
                }
                hintLabels[i].setVisible(true);
                break;
            }
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getHints() {
        return hints;
    }

    public void setHints(String[] hints) {
        this.hints = hints;
    }

    public Label[] getHintLabels() {
        return hintLabels;
    }

    public void setHintLabels(Label[] hintLabels) {
        this.hintLabels = hintLabels;
    }

    public boolean isHintRank() {
        return hintRank;
    }

    public void setHintRank(boolean hintRank) {
        this.hintRank = hintRank;
    }

    @Override
    public String toString() {
        return "ListQuestion{" +
                "hintLabels=" + Arrays.toString(hintLabels) +
                ", answer='" + answer + '\'' +
                ", question='" + question + '\'' +
                ", hints=" + Arrays.toString(hints) +
                ", hintRank=" + hintRank +
                '}';
    }
}
