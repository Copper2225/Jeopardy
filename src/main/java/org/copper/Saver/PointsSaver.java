package org.copper.Saver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import org.copper.ApplicationContext;
import org.copper.Buzzer.Team;
import org.copper.Play.PlayScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PointsSaver {
    public static void savePoints() {
        ObjectMapper mapper = new ObjectMapper();
        File fPoints = new File("src/main/resources/savings/points.json");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(fPoints, PlayScreen.getTeams());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadPoints(List<Team> teams, ObservableList<String> teamNames){
        ObjectMapper mapper = new ObjectMapper();
        try {
            File fPoints = new File("src/main/resources/" + "savings/points" +".json");
            ArrayList<Team> teamsFile = mapper.readValue(fPoints, new TypeReference<>() {});
            for (int i = 0; i < ApplicationContext.getTeamAmount(); i++){
                teams.add(new Team(teamsFile.get(i).getTeamName(), teamsFile.get(i).getiPAddress(), teamsFile.get(i).pointsProperty().get()));
                teamNames.add(teamsFile.get(i).getTeamName());
            }
        } catch (Exception e) {
            for (int i = 0; i < ApplicationContext.getTeamAmount(); i++){
                teams.add(new Team("Team " + (i + 1)));
                teamNames.add("Team " + (i + 1));
            }
            savePoints();
        }
    }
}
