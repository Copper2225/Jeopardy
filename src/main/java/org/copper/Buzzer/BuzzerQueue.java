package org.copper.Buzzer;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

public class BuzzerQueue {
    private static final Queue<Team> queue = new LinkedList<>();
    private static final Set<Team> set = new HashSet<>();
    private static final Consumer<Team> onFirstElementChange = team -> System.out.println(team.getTeamName());

    public BuzzerQueue() {
    }

    public boolean offer(Team element) {
        if (!set.contains(element)) {
            boolean wasEmpty = queue.isEmpty();
            queue.offer(element);
            set.add(element);
            if (wasEmpty) {
                onFirstElementChange.accept(element);
            }
            return true;
        }
        return false;
    }

    public Team poll() {
        Team element = queue.poll();
        if (element != null) {
            set.remove(element);
            if (!queue.isEmpty()) {
                onFirstElementChange.accept(queue.peek());
            } else {
                onFirstElementChange.accept(null);
            }
        }
        return element;
    }

    public void clear() {
        queue.clear();
        set.clear();
        onFirstElementChange.accept(null);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Team peek() {
        return queue.peek();
    }
}
