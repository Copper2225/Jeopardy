package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextQuestion.class, name = ApplicationContext.QuestionTypes.TEXT),
        @JsonSubTypes.Type(value = BildQuestion.class, name = ApplicationContext.QuestionTypes.BILD),
        @JsonSubTypes.Type(value = AudioQuestion.class, name = ApplicationContext.QuestionTypes.AUDIO)
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Question {
    private final String type;
    private int points;
    private boolean buzzer;

    public Question(@JsonProperty("type") String type, @JsonProperty("points") int points, @JsonProperty("buzzer") boolean buzzer) {
        this.type = type;
        this.points = points;
        this.buzzer = buzzer;
    }

    public String getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isBuzzer() {
        return buzzer;
    }

    public void setBuzzer(boolean buzzer) {
        this.buzzer = buzzer;
    }

    public void showQuestion(){
        if (buzzer){
            BuzzerQueue.setAllowBuzzer(true);
        }
    };

    public void showSolution(){
        if (buzzer){
            System.out.println("DISALLOW");
            BuzzerQueue.setAllowBuzzer(false);
        }
    };

    @Override
    public abstract String toString();
}
