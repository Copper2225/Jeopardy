package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.copper.ApplicationContext;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextQuestion.class, name = ApplicationContext.QuestionTypes.TEXT),
        @JsonSubTypes.Type(value = BildQuestion.class, name = ApplicationContext.QuestionTypes.BILD)
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Question {
    private final String type;
    private int points;

    public Question(@JsonProperty("type") String type, @JsonProperty("points") int points) {
        this.type = type;
        this.points = points;
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

    abstract public void showQuestion();

    @Override
    public abstract String toString();
}
