package jglt.io.events;

import jglt.io.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * Provides a more user-friendly way (other than GLFW) to get key events.
 */
public class Events {
    private static boolean initialized = false;
    private static final List<Event> events = new ArrayList<>();

    public static void init() {
        if (initialized) {
            return;
        }

        glfwSetKeyCallback(Window.getWindow(), new EventCallback());

        initialized = true;
    }

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
     * @param event The Event to add
     */
    static void addEvent(Event event) {
        events.add(event);
    }
}
