package org.copper.Buzzer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.copper.Saver.PointsSaver;

public class Team {
    private String teamName;
    private String iPAddress;
    private final transient IntegerProperty points = new SimpleIntegerProperty(0);  // Use transient for Jackson

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
        this.points.set(points);  // Deserialize points as an int
        savePoints();
    }

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

    @Override
    public String toString() {
        return "Team{" +
                "teamName='" + teamName + '\'' +
                ", iPAddress='" + iPAddress + '\'' +
                ", points=" + points.get() +
                '}';
    }
}
