package org.copper.Buzzer;

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

    public BuzzerQueue() {
    }

    public boolean offer(Team element) {
        if (!set.contains(element)) {
            queue.offer(element);
            set.add(element);
            PlayScreen.gettB().buzzer(element);
            System.out.println(element.getiPAddress());
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
            }
        }
        return element;
    }

    public static void clear() {
        queue.clear();
        set.clear();
        PlayScreen.gettB().clearBuzzer();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public static Team peek() {
        return queue.peek();
    }
}
