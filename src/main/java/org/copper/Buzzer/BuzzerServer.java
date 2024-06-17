package org.copper.Buzzer;

import static spark.Spark.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            System.out.println("IP-Adresse des Clients: " + ipAddress);
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

        buzzers.add(new Buzzer("Team 1", "h"));
//        buzzers.add(new Buzzer("Gfgjhkdgksfsdfhskldfdssfs", "h"));
//        buzzers.add(new Buzzer("Gfgjhkdgksfs dfhskldfdssfs", "h"));
//        buzzers.add(new Buzzer("Gfgjhkdgksfsdfhskldfdssfs", "h"));
//        buzzers.add(new Buzzer("Gfgjhkdgksfsdfhskldfdssfs", "h"));
//        buzzers.add(new Buzzer("Gfgjhkdgksfsdfhskldfdssfs", "h"));
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
