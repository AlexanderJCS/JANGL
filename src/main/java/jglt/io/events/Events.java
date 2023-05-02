package jglt.io.events;

import jglt.io.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

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

    public static List<Event> getEvents() {
        List<Event> eventsDeepcopy = new ArrayList<>(events);
        events.clear();

        return eventsDeepcopy;
    }

    static void addEvent(Event event) {
        events.add(event);
    }
}
