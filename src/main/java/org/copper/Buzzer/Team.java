package org.copper.Buzzer;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Team {
    private String teamName;
    private String iPAddress;
    private transient IntegerProperty points = new SimpleIntegerProperty(0);

    public Team(String teamName, String iPAddress) {
        this.teamName = teamName;
        this.iPAddress = iPAddress;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setiPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public IntegerProperty getPoints() {
        return points;
    }

    public String getiPAddress() {
        return iPAddress;
    }

    @Override
    public String toString() {
        return "Buzzer{" +
                "teamName='" + teamName + '\'' +
                ", iPAddress='" + iPAddress + '\'' +
                '}';
    }
}