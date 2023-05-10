package jglt.io;

import java.util.ArrayList;
import java.util.List;

public class EventContainer {
    private static final List<Event> events = new ArrayList<>();

    /**
     * @return A list of key events since last getEvents() call
     */
    public static List<Event> getEvents() {
        List<Event> eventsDeepcopy = new ArrayList<>(events);
        events.clear();

        return eventsDeepcopy;
    }

    /**
     * A package-protected method to add an event to the Events class
     * @param event The MouseEvent to add
     */
    public static void addEvent(Event event) {
        events.add(event);
    }
}
