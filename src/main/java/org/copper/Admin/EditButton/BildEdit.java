package org.copper.Admin.EditButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.copper.Admin.AdminScreen;
import org.copper.ApplicationContext;
import org.copper.Buzzer.BuzzerQueue;
import org.copper.Questions.BildQuestion;
import org.copper.Questions.Question;
import org.copper.Questions.Questions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class BildEdit extends EditPane {
    private final ImageView preview;
    private final FileChooser fileChooser;
    private ComboBox<String> listFiles;
    private final TextArea solutionArea;
    private final TextArea questionArea;
    private File file;

    public BildEdit() {
        super();
        Button upload = new Button("Hochladen");
        fileChooser = new FileChooser();
        preview = new ImageView();
        preview.setPreserveRatio(true);
        ApplicationContext.bindProperties(preview.fitWidthProperty(), ApplicationContext.Layouts.WITDH, 20, 1, 0);
        preview.fitHeightProperty().bind(AdminScreen.getAdminStage().heightProperty().divide(2.5));
        solutionArea = new TextArea();
        questionArea = new TextArea();
        loadFiles();
        upload.setOnAction((event -> {
            upload();
        }));
        VBox vBox = new VBox(new HBox(upload, listFiles), typeChoose, questionArea, solutionArea, preview);
        vBox.setAlignment(Pos.TOP_CENTER);
        pane = vBox;
    }

    private void upload() {
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Path source = Paths.get(file.getAbsolutePath());
            Path target = Paths.get("src/main/resources/quizzes/" + ApplicationContext.getQuizName() + "/images/" + file.getName());
            try {
                if (!Files.exists(target)) {
                    Files.copy(source, target);
                }
                Image image = new Image(target.toUri().toString());
                preview.setImage(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadFiles(){
        File resourcesDirectory = new File("src/main/resources/quizzes/" + ApplicationContext.getQuizName() + "/images");
        String[] fileNames = resourcesDirectory.list();
        listFiles = new ComboBox<String>();
        if(fileNames != null){
            listFiles.getItems().addAll(fileNames);
        }
        listFiles.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                file = new File("src/main/resources/quizzes/" + ApplicationContext.getQuizName() + "/images/" + t1);
                preview.setImage(new Image(file.toURI().toString()));
            }
        });
    }

    @Override
    public void loadSpecific(Question question) {
        if (question instanceof BildQuestion){
            listFiles.setValue(((BildQuestion) question).getFilename());
            questionArea.setText(((BildQuestion) question).getQuestion());
            solutionArea.setText(((BildQuestion) question).getAnswer());
            ((BildQuestion) question).setAnswer(solutionArea.getText());
        }
    }

    @Override
    public void save(int[] index) {
        Questions.getQuestions()[index[0]][index[1]] = new BildQuestion(file.getName(),questionArea.getText(), solutionArea.getText(), ApplicationContext.getPointMatrix()[index[0]][index[1]], Arrays.asList(BuzzerQueue.getBuzzerStates()).indexOf(typeChoose.getValue()));
    }
}
