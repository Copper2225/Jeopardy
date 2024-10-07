package org.copper.Saver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.copper.ApplicationContext;
import org.copper.Buzzer.Team;
import org.copper.Play.PlayScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class CategoriesSaver {
    public static void saveCategories(GridPane grid) {
        ObjectMapper mapper = new ObjectMapper();
        File fPoints = new File("src/main/resources/savings/categories.json");
        try {
            boolean[][] disabledMatrix = new boolean[ApplicationContext.getColumns()][ApplicationContext.getRows()];
            for (Node node : grid.getChildren()) {
                disabledMatrix[GridPane.getColumnIndex(node)][GridPane.getRowIndex(node)] = node.isDisabled();
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(fPoints, disabledMatrix);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static boolean[][] loadCategories(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            File fPoints = new File("src/main/resources/" + "savings/categories" +".json");
            boolean[][] teamsFile = mapper.readValue(fPoints, new TypeReference<>() {});
            return teamsFile;
        } catch (Exception e) {
            return new boolean[ApplicationContext.getColumns()][ApplicationContext.getRows()];
        }
    }
}
