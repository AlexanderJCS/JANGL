package jglt.io;

import java.util.ArrayList;
import java.util.List;

public class EventContainer {
    private static final List<Event> events = new ArrayList<>();
    private static int maxEvents = 1024;

    /**
     * @return A list of key events since last getEvents() call
     */
    public static List<Event> getEvents() {
        List<Event> eventsDeepcopy = new ArrayList<>(events);
        events.clear();

        return eventsDeepcopy;
    }

    /**
     * @param n The maximum number of events until some are removed to save memory. Set a negative number to have an
     *          infinite size.
     */
    public void setMaxEvents(int n) {
        maxEvents = n;
    }

    public int getMaxEvents() {
        return maxEvents;
    }

    /**
     * A package-protected method to add an event to the Events class. Will remove events if it is over
     * maxEvents.
     * @param event The MouseEvent to add
     */
    public static void addEvent(Event event) {
        events.add(event);

        if (events.size() > maxEvents && maxEvents >= 0) {
            events.remove(0);
        }
    }
}
