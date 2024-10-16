package org.copper.Admin.EditButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Questions.AudioQuestion;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class AudioEdit extends EditPane {
    private final FileChooser fileChooser;
    private ComboBox<String> listFiles;
    private final TextArea solutionArea;
    private final TextArea questionArea;
    private final TextField dauer;
    private File file;
    private final CheckBox buzzer;

    public AudioEdit() {
        super();
        Button upload = new Button("Hochladen");
        fileChooser = new FileChooser();
        solutionArea = new TextArea();
        questionArea = new TextArea();
        buzzer = new CheckBox("Buzzerfrage");
        loadFiles();
        upload.setOnAction((event -> {
            upload();
        }));
        dauer = new TextField();
        VBox vBox = new VBox(new HBox(typeChoose, upload, listFiles, dauer), buzzer, questionArea, solutionArea);
        vBox.setAlignment(Pos.TOP_CENTER);
        pane = vBox;
    }

    private void upload() {
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Path source = Paths.get(file.getAbsolutePath());
            Path target = Paths.get("src/main/resources/quizzes/"+ ApplicationContext.getQuizName() + "/audios/" + file.getName());
            try {
                if (!Files.exists(target)) {
                    Files.copy(source, target);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadFiles(){
        File resourcesDirectory = new File("src/main/resources/quizzes/"+ ApplicationContext.getQuizName() + "/audios");
        String[] fileNames = resourcesDirectory.list();
        listFiles = new ComboBox<String>();
        if(fileNames != null){
            listFiles.getItems().addAll(fileNames);
        }
        listFiles.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                file = new File("src/main/resources/quizzes/"+ ApplicationContext.getQuizName() + "/audios/" + t1);
            }
        });
    }

    @Override
    public void loadSpecific(Question question) {
        if (question instanceof AudioQuestion aQ){
            listFiles.setValue(aQ.getFilename());
            if(aQ.getDauer() != null) {
                dauer.setText(((AudioQuestion) question).getDauer().toString());
            }
            questionArea.setText(aQ.getQuestion());
            solutionArea.setText(aQ.getAnswer());
            aQ.setAnswer(solutionArea.getText());
        }
    }

    @Override
    public void save(int[] index) {
        Integer dauer = this.dauer.getText().isBlank() ? null : Integer.parseInt(this.dauer.getText());
        Questions.getQuestions()[index[0]][index[1]] = new AudioQuestion(file.getName(),questionArea.getText(), solutionArea.getText(), ApplicationContext.getPointMatrix()[index[0]][index[1]], Arrays.asList(BuzzerQueue.getBuzzerStates()).indexOf(typeChoose.getValue()), dauer);
    }
}
