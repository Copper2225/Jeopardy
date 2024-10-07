package org.copper.Buzzer;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import org.copper.Admin.AdminPlay.AdminPlayScene;
import org.copper.Admin.AdminScreen;
import org.copper.Play.PlayScreen;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class BuzzerQueue {
    private static final Queue<Team> queue = new LinkedList<>();
    private static final Set<Team> set = new HashSet<>();
    private static final BooleanProperty allowBuzzer = new SimpleBooleanProperty(false);

    public BuzzerQueue() {
    }

    public boolean offer(Team element) {
        if (!set.contains(element)) {
            boolean wasEmpty = queue.isEmpty();
            queue.offer(element);
            set.add(element);
            if(wasEmpty){
                PlayScreen.gettB().buzzer(element);
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
