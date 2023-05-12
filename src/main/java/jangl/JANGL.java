package jangl;

import jangl.io.keyboard.Keyboard;
import jangl.io.mouse.Mouse;
import jangl.io.Window;

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
