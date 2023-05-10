package jglt;

import jglt.io.Keyboard;
import jglt.io.Mouse;
import jglt.io.Window;
import jglt.io.events.Events;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class JGLT {
    private static boolean initialized = false;

    private JGLT() {}

    public static void init(int screenWidth, int screenHeight) {
        if (initialized) {
            return;
        }

        Mouse.init();
        Keyboard.init();
        Window.init(screenWidth, screenHeight);
        Events.init();

        initialized = true;
    }

    public static void update() {
        glfwPollEvents();
        glfwSwapBuffers(Window.getWindow());
    }
}
