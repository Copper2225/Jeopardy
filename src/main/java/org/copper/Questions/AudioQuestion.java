package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.image.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;
import javafx.animation.TranslateTransition;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.copper.ApplicationContext.createSpacer;

public class AudioQuestion extends Question {
    private String question;
    private String answer;
    @JsonIgnore
    private MediaPlayer mediaPlayer;
    @JsonIgnore
    private Media audio;
    @JsonIgnore
    private Image image;
    private String filename;

    @JsonCreator
    public AudioQuestion(
            @JsonProperty("filename") String filename,
            @JsonProperty("question") String question,
            @JsonProperty("answer") String answer,
            @JsonProperty("points") int points,
            @JsonProperty("buzzer") boolean buzzer
    ) {
        super(ApplicationContext.QuestionTypes.AUDIO, points, buzzer);
        this.filename = filename;
        Path target = Paths.get("src/main/resources/audios/" + filename);
        audio = new Media(target.toUri().toString());
        image = new Image("file:///C:/Users/Verenkotte/IdeaProjects/JeopardyGit/src/main/resources/music.png");
        this.question = question;
        this.answer = answer;
    }

    @Override
    public void showQuestion() {
        super.showQuestion();
        Label question = new Label(getQuestion());
        question.getStyleClass().add("topLabel");
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.fitHeightProperty().bind(PlayScreen.getPlayStage().heightProperty().subtract(PlayScreen.gettB().getRoot().heightProperty()).subtract(PlayScreen.getLogo().fitHeightProperty()).subtract(350));
        VBox vBox = new VBox(question, createSpacer(false), animateImage(), createSpacer(false));
        question.prefWidthProperty().bind(vBox.widthProperty());
        imageView.fitWidthProperty().bind(PlayScreen.getPlayStage().widthProperty().subtract(300));
        vBox.getStyleClass().addAll("stackQuestion", "bildQuestion");
        PlayScreen.setChildRoot(vBox);
    }

    @Override
    public void showSolution(){
        mediaPlayer.stop();
        Label question = new Label(getQuestion());
        question.getStyleClass().add("topLabel");
        Label solution = new Label(getAnswer());
        solution.getStyleClass().add("textQuestion");
        VBox vBox = new VBox(question, createSpacer(false), solution, createSpacer(false));
        question.prefWidthProperty().bind(vBox.widthProperty());
        vBox.getStyleClass().addAll("stackQuestion", "bildQuestion");
        PlayScreen.setChildRoot(vBox);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private StackPane animateImage(){
        mediaPlayer = new MediaPlayer(audio);

        File imageFile = new File("src/main/resources/" + "music.png");
        Image originalImage = new Image(imageFile.toURI().toString());

        // Erstellen eines WritableImage basierend auf dem Originalbild
        WritableImage writableImage = new WritableImage(
                (int) originalImage.getWidth(),
                (int) originalImage.getHeight());

        PixelReader pixelReader = originalImage.getPixelReader();
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        // Kopieren der Pixel des Originalbilds in das WritableImage
        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                pixelWriter.setArgb(x, y, pixelReader.getArgb(x, y));
            }
        }

        ImageView imageView = new ImageView(writableImage);

        double totalDurationMillis = 10000; // Gesamtdauer in Millisekunden (10 Sekunden)
        double millisecondsPerX = totalDurationMillis / originalImage.getWidth();

        // Timeline für die Animation erstellen
        Timeline timeline = new Timeline();
        for (int x = 0; x < originalImage.getWidth(); x++) {
            final int currentX = x;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(millisecondsPerX * x), event -> {
                for (int y = 0; y < originalImage.getHeight(); y++) {
                    Color color = pixelReader.getColor(currentX, y);
                    if (isApproximatelyWhite(color)) {
                        pixelWriter.setColor(currentX, y, Color.rgb(50, 50, 50)); // Weiß zu Rot ändern
                    }
                }
            });
            // Create a timeline to reduce volume gradually
            timeline.getKeyFrames().add(keyFrame);
        }
        Duration startFadeOutTime = Duration.seconds(9);
        Duration fadeOutDuration = Duration.seconds(0.8);
        KeyFrame volume = new KeyFrame(startFadeOutTime, new KeyValue(mediaPlayer.volumeProperty(), 1.0));
        KeyFrame low = new KeyFrame(startFadeOutTime.add(fadeOutDuration), new KeyValue(mediaPlayer.volumeProperty(), 0.0));
        timeline.getKeyFrames().addAll(volume, low);

        timeline.setOnFinished((event) -> {
            mediaPlayer.setVolume(1);
            mediaPlayer.pause();
        });


        mediaPlayer.setOnReady(() -> {
            // Aktionen, die ausgeführt werden sollen, wenn die Medien bereit sind
            System.out.println("Media is ready!");
            mediaPlayer.play();
            timeline.play();
        });
        return new StackPane(imageView);
    }

    private boolean isApproximatelyWhite(Color color) {
        return color.getRed() > 1.0 - 0.1 &&
                color.getGreen() > 1.0 - 0.1 &&
                color.getBlue() > 1.0 - 0.1 &&
                color.getOpacity() > 0;
    }

    @Override
    public String toString() {
        return "AudioQuestion{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", audio=" + mediaPlayer +
                ", image=" + image +
                ", filename='" + filename + '\'' +
                '}';
    }
}
