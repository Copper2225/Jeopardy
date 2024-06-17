package org.copper.Buzzer;

import static spark.Spark.*;
import static spark.Spark.stop;

import com.google.gson.Gson;
import spark.Spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BuzzerServer {
    private static List<Buzzer> buzzers = new ArrayList<>();
    public static void initialize(){
        port(4567);

        // Route für die HTML-Seite
        get("/", (req, res) -> {
            res.type("text/html");
            return new java.util.Scanner(new java.io.File("src/main/resources/index.html")).useDelimiter("\\Z").next();
        });

        // Route zum Empfangen des Buzz
        post("/buzz", (req, res) -> {
            String ipAddress = req.ip();
            Optional<Buzzer> optionalBuzzer = buzzers.stream().filter((buzzer -> buzzer.iPAddress.equals(ipAddress))).findFirst();
            optionalBuzzer.ifPresent(buzzer -> System.out.println(buzzer.teamName + " hat gebuzzert"));
            return "Erfolgreich gebuzzert";
        });

        // Route zum Speichern der Eingabe
        post("/save", (req, res) -> {
            Gson gson = new Gson();
            InputData inputData = gson.fromJson(req.body(), InputData.class);
            buzzers.add(new Buzzer(inputData.getInput(), req.ip()));
            System.out.println(Arrays.deepToString(buzzers.toArray()));

            res.type("application/json");
            return gson.toJson(buzzers);
        });

        buzzers.add(new Buzzer("Team 1", "192.168.177.2"));
        buzzers.add(new Buzzer("Team 2", "192.168.177.2"));
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

    public static List<Buzzer> getBuzzers() {
        return buzzers;
    }
}
