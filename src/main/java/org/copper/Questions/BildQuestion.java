package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.Image;
import org.copper.ApplicationContext;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BildQuestion extends Question {
    @JsonIgnore
    private Image image;
    private String question;
    private String filename;
    private String answer;


    @JsonCreator
    public BildQuestion(
            @JsonProperty("filename") String filename,
            @JsonProperty("question") String question,
            @JsonProperty("answer") String answer,
            @JsonProperty("points") int points
    ) {
        super(ApplicationContext.QuestionTypes.BILD, points);
        this.filename = filename;
        Path target = Paths.get("src/main/resources/images/" + filename);
        System.out.println(target.toUri().toString());
        image = new Image(target.toUri().toString());
        this.answer = answer;
        this.question = question;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "BildQuestion{" +
                "image=" + image +
                ", question='" + question + '\'' +
                ", filename='" + filename + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
