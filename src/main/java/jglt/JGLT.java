package jglt;

import jglt.io.keyboard.Keyboard;
import jglt.io.mouse.Mouse;
import jglt.io.Window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class JGLT {
    private static boolean initialized = false;

    private JGLT() {}

    public static void init(int screenWidth, int screenHeight) {
        if (initialized) {
            return;
        }

        Window.init(screenWidth, screenHeight);
        Mouse.init();
        Keyboard.init();

        initialized = true;
    }

    public static void update() {
        glfwPollEvents();
        glfwSwapBuffers(Window.getWindow());
    }
}
