package jangl;

import jangl.graphics.Camera;
import jangl.graphics.postprocessing.PostProcessing;
import jangl.io.Window;
import jangl.io.keyboard.Keyboard;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.Scroll;
import jangl.sound.Sound;
import jangl.time.Clock;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class JANGL {
    private static boolean initialized = false;

    private JANGL() {
    }

    public static void init(int screenWidth, int screenHeight) {
        if (initialized) {
            return;
        }

        Window.init(screenWidth, screenHeight);
        Camera.init();
        Mouse.init();
        Scroll.init();
        Keyboard.init();
        Sound.init();

        initialized = true;
    }

    public static void update() {
        glfwPollEvents();
        glfwSwapBuffers(Window.getWindow());

        Clock.update();
    }
}
