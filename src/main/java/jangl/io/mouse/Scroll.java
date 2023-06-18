package jangl.io.mouse;

import jangl.io.EventsConfig;
import jangl.io.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

public class Scroll {
    private static final List<ScrollEvent> EVENTS = new ArrayList<>();
    private static boolean initialized = false;

    private Scroll() {
    }

    public static void init() {
        if (initialized) {
            return;
        }

        glfwSetScrollCallback(Window.getWindow(), new ScrollEventCallback());

        initialized = true;
    }

    public static boolean getInit() {
        return initialized;
    }

    static void addEvent(ScrollEvent event) {
        EVENTS.add(event);

        // Remove old events if the events list size is too large
        if (EVENTS.size() > EventsConfig.maxEvents) {
            EVENTS.remove(0);
        }
    }

    public static List<ScrollEvent> getEvents() {
        List<ScrollEvent> eventsDeepcopy = new ArrayList<>(EVENTS);
        EVENTS.clear();

        return eventsDeepcopy;
    }
}
