package jglt.io;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

/**
 * Provides a more user-friendly way to interface with keyboard IO operations other than GLFW
 */
public class Keyboard {
    private static boolean initialized = false;

    private Keyboard() {}

    public static void init() {
        if (getInit()) {
            return;
        }

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
}
