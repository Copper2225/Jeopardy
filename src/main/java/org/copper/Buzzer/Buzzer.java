package org.copper.Buzzer;

public class Buzzer {
    String teamName;
    String iPAddress;

    public Buzzer(String teamName, String iPAddress) {
        this.teamName = teamName;
        this.iPAddress = iPAddress;
    }

    public String getTeamName() {
        return teamName;
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
