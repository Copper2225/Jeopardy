package org.copper.Buzzer;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.Play.PlayScreen;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class BuzzerQueue {
    private static final Queue<Team> queue = new LinkedList<>();
    private static final Set<Team> set = new HashSet<>();
    private static final BooleanProperty allowBuzzer = new SimpleBooleanProperty(false);
    private static MediaPlayer mP;

    public BuzzerQueue() {
        Path target = Paths.get("src/main/resources/Buzzer.mp3");
        Media buzzSound = new Media(target.toUri().toString());
        mP = new MediaPlayer(buzzSound);
        mP.setOnEndOfMedia(() -> {
            mP.pause();
            mP.seek(Duration.ZERO);
        });
        mP.setVolume(0.6);
    }

    public boolean offer(Team element) {
        if (!set.contains(element)) {
            boolean wasEmpty = queue.isEmpty();
            queue.offer(element);
            set.add(element);
            if(wasEmpty){
                PlayScreen.gettB().buzzer(element);
                mP.play();
                Platform.runLater(() -> AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(element.getTeamName()));
            }
            AdminPlayScene.getEdit().addBuzzer(element);
            return true;
        }
        return false;
    }

    public static Team poll() {
        Team element = queue.poll();
        if (element != null) {
            set.remove(element);
            AdminPlayScene.getEdit().removeBuzzer();
            if(peek() != null){
                PlayScreen.gettB().buzzer(peek());
                AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(element.getTeamName());
            }else{
                PlayScreen.gettB().clearBuzzer();
                AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(null);
            }
        }
        return element;
    }

    public static void clear() {
        queue.clear();
        set.clear();
        AdminPlayScene.getQuest().getBuzzeringTeamProperty().set(null);
        PlayScreen.gettB().clearBuzzer();
    }

    public static boolean isAllowBuzzer() {
        return allowBuzzer.get();
    }

    public static BooleanProperty allowBuzzerProperty() {
        return allowBuzzer;
    }

    public static void setAllowBuzzer(boolean allowBuzzer) {
        BuzzerQueue.allowBuzzer.set(allowBuzzer);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public static Team peek() {
        return queue.peek();
    }

    public static Queue<Team> getQueue(){
        return queue;
    }
}
