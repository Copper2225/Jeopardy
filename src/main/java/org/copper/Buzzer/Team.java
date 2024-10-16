package org.copper.Buzzer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.Questions.ListQuestion;
import org.copper.Saver.PointsSaver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

public class Team {
    private String teamName;
    private String iPAddress;
    private final transient IntegerProperty points = new SimpleIntegerProperty(0);
    @JsonIgnore
    private final StringProperty buzzerPosition = new SimpleStringProperty("");
    @JsonIgnore
    private final BooleanProperty show = new SimpleBooleanProperty(false);

    private void savePoints() {
        points.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                PointsSaver.savePoints();
            }
        });
    }

    // Default constructor for Jackson
    public Team() {
        savePoints();
    }

    // Constructors with arguments
    public Team(String teamName) {
        this.teamName = teamName;
        savePoints();
    }

    public Team(String teamName, String iPAddress) {
        this.teamName = teamName;
        this.iPAddress = iPAddress;
        savePoints();
    }

    @JsonCreator
    public Team(@JsonProperty("teamName") String teamName,
                @JsonProperty("iPAddress") String iPAddress,
                @JsonProperty("points") int points) {
        this.teamName = teamName;
        this.iPAddress = iPAddress;
        this.points.set(points);
        savePoints();
    }

    public void updateBuzzerPosition(){
        Optional<Team> matchingTeam = BuzzerQueue.getQueue().stream()
                .filter(team -> team.equals(this))
                .findFirst();

        if (AdminPlayScene.getQuest().getQuestion() instanceof ListQuestion lQ) {
            System.out.println(buzzerPosition.getValue().trim().isEmpty());
            if(lQ.isHintRank()){
                long number = Arrays.stream(lQ.getHintLabels()).filter(Node::isVisible).count();
                if(matchingTeam.isPresent()){
                    if (buzzerPosition.getValue().trim().isEmpty()) Platform.runLater(() -> buzzerPosition.set(Long.toString(number)));
                } else {
                    Platform.runLater(() -> buzzerPosition.set(""));
                }
            }
        } else {
            Platform.runLater(() -> buzzerPosition.set(matchingTeam.map(team -> Integer.toString(((LinkedList<Team>) BuzzerQueue.getQueue()).indexOf(team)+1)).orElse("")));
        }
    };

    // Getter for teamName
    public String getTeamName() {
        return teamName;
    }

    // Setter for teamName
    public void setTeamName(String teamName) {
        this.teamName = teamName;
        PointsSaver.savePoints();
    }

    // Getter for iPAddress
    public String getiPAddress() {
        return iPAddress;
    }

    // Setter for iPAddress
    public void setiPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
        PointsSaver.savePoints();
    }

    // Getter for points as int (for Jackson)
    public int getPoints() {
        return points.get();
    }

    // Setter for points as int (for Jackson)
    public void setPoints(int points) {
        this.points.set(points);
    }

    // Getter for the IntegerProperty (not used by Jackson)
    public IntegerProperty pointsProperty() {
        return points;
    }

    public String getBuzzerPosition() {
        return buzzerPosition.get();
    }

    public StringProperty buzzerPositionProperty() {
        return buzzerPosition;
    }

    public void setBuzzerPosition(String buzzerPosition) {
        this.buzzerPosition.set(buzzerPosition);
    }

    public boolean isShow() {
        return show.get();
    }

    public BooleanProperty showProperty() {
        return show;
    }

    public void setShow(boolean show) {
        this.show.set(show);
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamName='" + teamName + '\'' +
                ", iPAddress='" + iPAddress + '\'' +
                ", points=" + points.get() +
                '}';
    }
}
