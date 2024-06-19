package org.copper.Buzzer;

import static spark.Spark.*;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import spark.Spark;

import java.util.*;

public class BuzzerServer {
    private static List<Team> teams = new ArrayList<>();
    private static BuzzerQueue buzzerQueue = new BuzzerQueue();
    private static ObservableList<String> buzzerNames = FXCollections.observableArrayList();
    public static void initialize(){
        port(4567);

        // Route für die HTML-Seite
        get("/", (req, res) -> {
            res.type("text/html");
            return new java.util.Scanner(new java.io.File("src/main/resources/index.html")).useDelimiter("\\Z").next();
        });

        // Route zum Empfangen des Buzz
        post("/buzz", (req, res) -> {
            if(BuzzerQueue.isAllowBuzzer()){
                String ipAddress = req.ip();
                System.out.println(Arrays.deepToString(teams.toArray()));
                Optional<Team> optionalTeam = teams.stream().filter((team -> team.getiPAddress().equals(ipAddress))).findFirst();
                optionalTeam.ifPresent(team -> buzzerQueue.offer(team));
                return "Erfolgreich gebuzzert";
            }
            else return "Buzzer nicht freigegeben";
        });

        // Route zum Speichern der Eingabe
        post("/save", (req, res) -> {
            Gson gson = new Gson();
            InputData inputData = gson.fromJson(req.body(), InputData.class);
            teams.add(new Team(inputData.getInput(), req.ip()));
            buzzerNames.add(inputData.getInput());
            res.type("application/json");
            return gson.toJson(teams);
        });
    }

    public static void stop(){
        Spark.stop();
    }

    static class InputData {
        private String input;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }

    public static List<Team> getBuzzers() {
        return teams;
    }

    public static List<Team> getTeams() {
        return teams;
    }

    public static BuzzerQueue getBuzzerQueue() {
        return buzzerQueue;
    }

    public static ObservableList<String> getBuzzerNames() {
        return buzzerNames;
    }
}
