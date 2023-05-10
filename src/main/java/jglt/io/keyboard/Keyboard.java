package jglt.io.keyboard;

import jglt.io.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * Provides a more user-friendly way to interface with keyboard IO operations other than GLFW
 */
public class Keyboard {
    private static boolean initialized = false;
    private static final List<KeyEvent> events = new ArrayList<>();

    private Keyboard() {}

    public static void init() {
        if (getInit()) {
            return;
        }

        glfwSetKeyCallback(Window.getWindow(), new KeyboardEventCallback());
        initialized = true;
    }

    /**
     * @return If the program is initialized
     */
    public static boolean getInit() {
        return initialized;
    }

    /**
     * @param glfwKey The key ID. You can find these using GLFW.GLFW_KEY_[key]
     * @return True if the key is down. False otherwise.
     */
    public static boolean getKeyDown(int glfwKey) {
        return glfwGetKey(Window.getWindow(), glfwKey) == 1;
    }

    /**
     * @return A list of key events since last getEvents() call
     */
    public static List<KeyEvent> getEvents() {
        List<KeyEvent> eventsDeepcopy = new ArrayList<>(events);
        events.clear();

        return eventsDeepcopy;
    }

    /**
     * A package-protected method to add an event to the Events class
     * @param event The KeyEvent to add
     */
    static void addEvent(KeyEvent event) {
        events.add(event);
    }
}
