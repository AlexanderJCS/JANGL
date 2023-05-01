package jglt.io;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

/**
 *
 */
public class Keyboard {
    private static boolean initialized = false;

    public static void init() {
        if (getInit()) {
            return;
        }

        initialized = true;
    }

    public static boolean getInit() {
        return initialized;
    }

    public static boolean getKeyDown(int glfwKey) {
        return glfwGetKey(Window.getWindow(), glfwKey) == 1;
    }
}
