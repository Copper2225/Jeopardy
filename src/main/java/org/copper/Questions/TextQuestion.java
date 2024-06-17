package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.copper.ApplicationContext;

public class TextQuestion extends Question {
    private String question;
    private String answer;

    @JsonCreator
    public TextQuestion(
            @JsonProperty("question") String question,
            @JsonProperty("answer") String answer
    ) {
        super(ApplicationContext.QuestionTypes.TEXT);
        this.question = question;
        this.answer = answer;
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
