package jglt;

import jglt.io.keyboard.Keyboard;
import jglt.io.mouse.Mouse;
import jglt.io.Window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class JANGL {
    private static boolean initialized = false;

    private JANGL() {}

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
