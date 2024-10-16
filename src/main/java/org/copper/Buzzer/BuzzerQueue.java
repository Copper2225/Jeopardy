package org.copper.Buzzer;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.ApplicationContext;
import org.copper.Play.PlayScreen;
import org.copper.Questions.ListQuestion;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BuzzerQueue {
    private static final Queue<Team> queue = new LinkedList<>();
    private static final Set<Team> set = new HashSet<>();
    private static final BooleanProperty allowBuzzer = new SimpleBooleanProperty(false);
    private static MediaPlayer mP;
    private static String[] buzzerStates = {"", "Buzzer", "Queue"};
    private static final StringProperty currentBuzzerStatus = new SimpleStringProperty(buzzerStates[0]);

    public BuzzerQueue() {
        Path target = Paths.get("src/main/resources/Buzzer.mp3");
        Media buzzSound = new Media(target.toUri().toString());
        mP = new MediaPlayer(buzzSound);
        mP.setOnEndOfMedia(() -> {
            mP.stop();
            mP.seek(Duration.ZERO);
        });
        mP.setVolume(0.6);
    }

    private static void updateAllTeamsPositions() {
        for (Team t: PlayScreen.getTeams()) {
            t.updateBuzzerPosition();
        }
    }

    public void offer(Team element) {
        if (!set.contains(element)) {
            boolean wasEmpty = queue.isEmpty();
            queue.offer(element);
            set.add(element);
            updateAllTeamsPositions();
            if(wasEmpty && currentBuzzerStatus.isEqualTo(buzzerStates[1]).get()){
                PlayScreen.getTb().buzzer(element);
                mP.play();
                Platform.runLater(() -> AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(element.getTeamName()));
            } else if(wasEmpty){
                Platform.runLater(() -> AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(element.getTeamName()));
            }
            AdminPlayScene.getEdit().addBuzzer(element);
        }
    }

    public static void poll() {
        Team element = queue.poll();
        if (element != null) {
            set.remove(element);
            AdminPlayScene.getEdit().removeBuzzer();
            updateAllTeamsPositions();
            if(peek() != null){
                if(BuzzerQueue.currentBuzzerStatusProperty().isEqualTo(BuzzerQueue.getBuzzerStates()[1]).get()){
                    PlayScreen.getTb().buzzer(peek());
                }
                AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(peek().getTeamName());
            }else{
                PlayScreen.getTb().clearBuzzer();
                AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(null);
            }
        } else {
            AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(null);
        }
    }

    public static void clear() {
        queue.clear();
        set.clear();
        updateAllTeamsPositions();
        AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(null);
        PlayScreen.getTb().clearBuzzer();
    }

    public static void toggleStatus() {
        if(currentBuzzerStatus.get().equals(buzzerStates[0])){
            currentBuzzerStatus.set(buzzerStates[1]);
            setAllowBuzzer(true);
        } else if (currentBuzzerStatus.get().equals(buzzerStates[1])) {
            currentBuzzerStatus.set(buzzerStates[2]);
            setAllowBuzzer(true);
        } else {
            currentBuzzerStatus.set(buzzerStates[0]);
            setAllowBuzzer(false);
        }
    }

    public static void setStatus(int status) {
        currentBuzzerStatus.set(buzzerStates[status]);
        if(status == 1 || status == 2){
            setAllowBuzzer(true);
        }
        if(status == 2) {
            AdminPlayScene.getInputs().showInputsProperty().set(true);
        }
    }

    public static boolean isAllowBuzzer() {
        return allowBuzzer.get();
    }

    public static void setAllowBuzzer(boolean allowBuzzer) {
        BuzzerQueue.allowBuzzer.set(allowBuzzer);
    }

    public static Team peek() {
        return queue.peek();
    }

    public static Queue<Team> getQueue(){
        return queue;
    }

    public static String[] getBuzzerStates() {
        return buzzerStates;
    }

    public static StringProperty currentBuzzerStatusProperty() {
        return currentBuzzerStatus;
    }

    public static MediaPlayer getmP() {
        return mP;
    }

    public static void setmP(MediaPlayer mP) {
        BuzzerQueue.mP = mP;
    }

    public static void setBuzzerStates(String[] buzzerStates) {
        BuzzerQueue.buzzerStates = buzzerStates;
    }

    public static void setCurrentBuzzerStatus(String currentBuzzerStatus) {
        BuzzerQueue.currentBuzzerStatus.set(currentBuzzerStatus);
    }
}
