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
        @JsonSubTypes.Type(value = AudioQuestion.class, name = ApplicationContext.QuestionTypes.AUDIO),
        @JsonSubTypes.Type(value = ChoiceQuestion.class, name = ApplicationContext.QuestionTypes.CHOICE),
        @JsonSubTypes.Type(value = ListQuestion.class, name = ApplicationContext.QuestionTypes.LIST)
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Question {
    private final String type;
    private int points;
    private int buzzer;

    public Question(@JsonProperty("type") String type, @JsonProperty("points") int points, @JsonProperty("buzzer") int buzzer) {
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

    public int getBuzzer() {
        return buzzer;
    }

    public void setBuzzer(int buzzer) {
        this.buzzer = buzzer;
    }

    public void showQuestion(){
        AdminPlayScene.getQuest().setPointsValue(points);
        BuzzerQueue.setStatus(buzzer);
    };

    public void showSolution(){
        BuzzerQueue.setAllowBuzzer(false);
    };

    @Override
    public abstract String toString();
}
