package org.copper.Buzzer;

import static spark.Spark.*;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.Play.PlayScreen;
import spark.Spark;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class BuzzerServer {
    private static List<Team> teams = new ArrayList<>();
    private static BuzzerQueue buzzerQueue = new BuzzerQueue();
    private static ObservableList<String> buzzerNames = FXCollections.observableArrayList();
    public static void initialize() throws UnknownHostException {
        port(4567);

        System.out.println(InetAddress.getLocalHost().getHostAddress());

        // Route für die HTML-Seite
        get("/", (req, res) -> {
            res.type("text/html");
            return new java.util.Scanner(new java.io.File("src/main/resources/index.html")).useDelimiter("\\Z").next();
        });

        // Route zum Empfangen des Buzz
        post("/buzz", (req, res) -> {
            if(BuzzerQueue.isAllowBuzzer()){
                String ipAddress = req.ip();
                Optional<Team> optionalTeam = PlayScreen.getTeams().stream().filter((team -> Objects.equals(team.getiPAddress(), ipAddress))).findFirst();
                optionalTeam.ifPresent(team -> buzzerQueue.offer(team));
                return "Erfolgreich gebuzzert";
            }
            else return "Buzzer nicht freigegeben";
        });

        // Route zum Speichern der Eingabe
        post("/save", (req, res) -> {
            Gson gson = new Gson();
            InputData inputData = gson.fromJson(req.body(), InputData.class);
            if (PlayScreen.getRoot().getChildren().get(1) == PlayScreen.getOverview().getRoot()){
                teams.addFirst(new Team(inputData.getInput(), req.ip()));
                buzzerNames.add(inputData.getInput());
            }else {
                if(!inputData.getInput().isEmpty()){
                    Optional<Team> name = PlayScreen.getTeams().stream().filter((team) -> {
                    String ipAddress = team.getiPAddress();
                    System.out.println(ipAddress + " " + req.ip());
                    return ipAddress != null && ipAddress.equals(req.ip());
                }).findFirst();
                    name.ifPresent(team -> {
                        int index = PlayScreen.getTeamNames().indexOf(team.getTeamName());
                        if(BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[2]).get()){
                            buzzerQueue.offer(team);
                        }
                        if(!AdminPlayScene.getInputs().isShowInputs() || (BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[2])).get() && !PlayScreen.getTeams().get(index).isShow() && !team.buzzerPositionProperty().isNotEmpty().get()){
                            Platform.runLater(() -> AdminPlayScene.getInputs().getInputTexts().set(index, inputData.getInput()));
                        }
                    });
                }
            }

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
