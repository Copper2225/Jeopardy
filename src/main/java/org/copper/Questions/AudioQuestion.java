package org.copper.Questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.scene.image.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.util.Duration;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;

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
    private final Media audio;
    private String filename;
    private Integer dauer;

    @JsonCreator
    public AudioQuestion(
            @JsonProperty("filename") String filename,
            @JsonProperty("question") String question,
            @JsonProperty("answer") String answer,
            @JsonProperty("points") int points,
            @JsonProperty("buzzer") int buzzer,
            @JsonProperty("dauer") Integer dauer
    ) {
        super(ApplicationContext.QuestionTypes.AUDIO, points, buzzer);
        this.filename = filename;
        Path target = Paths.get("src/main/resources/quizzes/" + ApplicationContext.getQuizName() + "/audios/" + filename);
        audio = new Media(target.toUri().toString());
        this.question = question;
        this.dauer = dauer;
        this.answer = answer;
    }

    @Override
    public void showQuestion() {
        super.showQuestion();
        Label question = new Label(getQuestion());
        question.getStyleClass().add("topLabel");
        VBox vBox = new VBox(question, createSpacer(false), animateImage(), createSpacer(false));
        question.prefWidthProperty().bind(vBox.widthProperty());
        vBox.getStyleClass().addAll("stackQuestion", "bildQuestion");
        AdminPlayScene.getQuest().getPlay().textProperty().bind(
                Bindings.when(mediaPlayer.statusProperty().isEqualTo(MediaPlayer.Status.PLAYING)).then("Playing").otherwise("Paused")
        );
        AdminPlayScene.getQuest().getPlay().styleProperty().bind(
                Bindings.when(mediaPlayer.statusProperty().isEqualTo(MediaPlayer.Status.PLAYING))
                        .then("-fx-background-color: lightgreen;")
                        .otherwise("-fx-background-color: lightcoral;")
        );
        PlayScreen.setChildRoot(vBox);
    }

    @Override
    public void showSolution(){
        super.showSolution();
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

        File imageFile = new File("src/main/resources/" + "WideMusic.png");
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

        Timeline timeline = new Timeline();

        mediaPlayer.setOnPaused(timeline::pause);
        mediaPlayer.setOnStopped(timeline::stop);
        mediaPlayer.setOnPlaying(timeline::play);
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && newValue.toMillis() == 0) {
                timeline.playFromStart();
            }
        });

        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.stop());

        mediaPlayer.setOnReady(() -> {
            // Aktionen, die ausgeführt werden sollen, wenn die Medien bereit sind
            double totalDurationMillis = dauer != null ? Duration.seconds(dauer).toMillis() : mediaPlayer.getTotalDuration().toMillis();
            double millisecondsPerX = totalDurationMillis / originalImage.getWidth();
            for (int x = 0; x < originalImage.getWidth(); x++) {
                final int currentX = x;
                KeyFrame keyFrame = new KeyFrame(Duration.millis(millisecondsPerX * x), event -> {
                    for (int y = 0; y < originalImage.getHeight(); y++) {
                        Color color = pixelReader.getColor(currentX, y);
                        if (isApproximatelyWhite(color)) {
                            pixelWriter.setColor(currentX, y, Color.rgb(200, 200, 200)); // Weiß zu Rot ändern
                        }
                    }
                });
                // Create a timeline to reduce volume gradually
                timeline.getKeyFrames().add(keyFrame);
            }
            Duration startFadeOutTime = Duration.millis(totalDurationMillis).subtract(Duration.seconds(1));
            Duration fadeOutDuration = Duration.seconds(1);
            KeyFrame volume = new KeyFrame(startFadeOutTime, new KeyValue(mediaPlayer.volumeProperty(), 1.0));
            KeyFrame low = new KeyFrame(startFadeOutTime.add(fadeOutDuration), new KeyValue(mediaPlayer.volumeProperty(), 0.0));
            timeline.getKeyFrames().addAll(volume, low);
            timeline.setOnFinished((event) -> {
                mediaPlayer.setVolume(1);
                mediaPlayer.pause();
            });
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
                ", filename='" + filename + '\'' +
                '}';
    }

    public Integer getDauer() {
        return dauer;
    }

    public void setDauer(Integer dauer) {
        this.dauer = dauer;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
