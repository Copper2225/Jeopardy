package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChoiceQuestion extends Question {

    @JsonIgnore
    private String answer;
    @JsonIgnore
    private final boolean forceUnderneath = true;
    @JsonIgnore
    List<String> shuffledOptions;
    private String question;
    private String[] solutions;
    private int correctIndex;


    @JsonCreator
    public ChoiceQuestion(
            @JsonProperty("question") String question,
            @JsonProperty("correctIndex") int correctIndex,
            @JsonProperty("solutions") String[] solutions,
            @JsonProperty("buzzer") int buzzer,
            @JsonProperty("points") int points
    ) {
        super(ApplicationContext.QuestionTypes.CHOICE, points, buzzer);
        this.question = question;
        this.correctIndex = correctIndex;
        this.solutions = solutions;
        shuffledOptions = Arrays.asList(Arrays.copyOf(solutions, solutions.length));
        Collections.shuffle(shuffledOptions);
        answer = String.valueOf((char)(65 + shuffledOptions.indexOf(solutions[correctIndex])));
    }

    @Override
    public void showQuestion() {
        super.showQuestion();
        Label question = new Label(getQuestion());
        question.getStyleClass().add("topLabel");
        GridPane gridPane = new GridPane();
        VBox questRoot = new VBox(question, gridPane);
        for(int i = 0 ; i < solutions.length; i++) {
            Label text = new Label(shuffledOptions.get(i));
            text.prefHeightProperty().bind(questRoot.heightProperty().subtract(question.heightProperty()).divide(solutions.length/2).subtract(30));
            text.getStyleClass().add("choiceText");
            Label letter = new Label(String.valueOf(Character.toChars(65 + i)));
            letter.prefHeightProperty().bind(questRoot.heightProperty().subtract(question.heightProperty()).divide(solutions.length/2).subtract(30));
            letter.prefWidthProperty().bind(questRoot.widthProperty().divide(2).subtract(40).multiply(0.1));
            letter.getStyleClass().add("choiceLetter");
            HBox choice = new HBox(letter, text);
            choice.getStyleClass().add("choiceBox");
            boolean horizontal = solutions.length%2 == 1 || forceUnderneath;
            choice.prefWidthProperty().bind(
                    Bindings.when(Bindings.createBooleanBinding(
                                    () -> horizontal,
                                    questRoot.widthProperty()
                            ))
                            .then(questRoot.widthProperty())
                            .otherwise(questRoot.widthProperty().divide(2)).subtract(40)
            );
            choice.prefHeightProperty().bind(questRoot.heightProperty().subtract(question.heightProperty()).divide(solutions.length/2).subtract(30));
            if(horizontal){
                VBox.setMargin(choice, new Insets(0, 30, 30, 30));
                questRoot.getChildren().add(choice);
            } else {
                gridPane.add(choice, i%2, i/2 );
            }
        }

        questRoot.getStyleClass().addAll("stackQuestion");
        question.prefWidthProperty().bind(questRoot.widthProperty());
        PlayScreen.setChildRoot(questRoot);
    }

    @Override
    public void showSolution(){
        super.showQuestion();
        Label question = new Label(getQuestion());
        question.getStyleClass().add("topLabel");
        GridPane gridPane = new GridPane();
        VBox questRoot = new VBox(question, gridPane);
        for(int i = 0 ; i < solutions.length; i++) {
            Label text = new Label(shuffledOptions.get(i));
            text.prefHeightProperty().bind(questRoot.heightProperty().subtract(question.heightProperty()).divide(solutions.length/2).subtract(30));
            text.getStyleClass().add("choiceText");
            Label letter = new Label(String.valueOf(Character.toChars(65 + i)));
            letter.prefHeightProperty().bind(questRoot.heightProperty().subtract(question.heightProperty()).divide(solutions.length/2).subtract(30));
            letter.prefWidthProperty().bind(questRoot.widthProperty().divide(2).subtract(40).multiply(0.1));
            letter.getStyleClass().add("choiceLetter");
            if(letter.getText().equals(answer)){
                letter.getStyleClass().add("correctLetter");
            }
            HBox choice = new HBox(letter, text);
            choice.getStyleClass().add("choiceBox");
            boolean horizontal = solutions.length%2 == 1 || forceUnderneath;
            choice.prefWidthProperty().bind(
                    Bindings.when(Bindings.createBooleanBinding(
                                    () -> horizontal,
                                    questRoot.widthProperty()
                            ))
                            .then(questRoot.widthProperty())
                            .otherwise(questRoot.widthProperty().divide(2)).subtract(40)
            );
            choice.prefHeightProperty().bind(questRoot.heightProperty().subtract(question.heightProperty()).divide(solutions.length/2).subtract(30));
            if(horizontal){
                VBox.setMargin(choice, new Insets(0, 30, 30, 30));
                questRoot.getChildren().add(choice);
            } else {
                gridPane.add(choice, i%2, i/2 );
            }
        }

        questRoot.getStyleClass().addAll("stackQuestion");
        question.prefWidthProperty().bind(questRoot.widthProperty());
        PlayScreen.setChildRoot(questRoot);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getSolutions() {
        return solutions;
    }

    public void setSolutions(String[] solutions) {
        this.solutions = solutions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }

    @Override
    public String toString() {
        return "ChoiceQuestion{" +
                "question='" + question + '\'' +
                ", solutions=" + Arrays.toString(solutions) +
                ", answer='" + answer + '\'' +
                '}';
    }
}
