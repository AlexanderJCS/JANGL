package jangl.io.keyboard;

import jangl.io.EventsConfig;
import jangl.io.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * Provides a more user-friendly way to interface with keyboard IO operations other than GLFW
 */
public class Keyboard extends EventsConfig {
    private static final List<KeyEvent> EVENTS = new ArrayList<>();
    private static boolean initialized = false;

    private Keyboard() {
    }

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
     * Checks if a certain character is being pressed on the keyboard. If you want to check a key that doesn't have
     * a character, like shift or an arrow key, use org.lwjgl.glfw.GLFW.GLFW_KEY_(key)
     *
     * @param key The character to get down.
     * @return True if the key is down. False otherwise.
     */
    public static boolean getKeyDown(char key) {
        return getKeyDown((int) Character.toUpperCase(key));
    }

    static void addEvent(KeyEvent event) {
        EVENTS.add(event);

        // Remove old events if the events list size is too large
        if (EVENTS.size() > EventsConfig.MAX_EVENTS) {
            EVENTS.remove(0);
        }
    }

    public static List<KeyEvent> getEvents() {
        List<KeyEvent> eventsDeepcopy = new ArrayList<>(EVENTS);
        EVENTS.clear();

        return eventsDeepcopy;
    }
}
