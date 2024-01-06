package jangl;

import jangl.graphics.Camera;
import jangl.graphics.shaders.ShaderProgram;
import jangl.io.Window;
import jangl.io.keyboard.Keyboard;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.Scroll;
import jangl.resourcemanager.ResourceManager;
import jangl.sound.Sound;
import jangl.time.Clock;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;

public class Jangl {
    private static boolean initialized = false;

    private Jangl() {
    }

    /**
     * Initializes Jangl using two ratios:
     * a. the ratio of the window height to the primary monitor height
     * b. the aspect ratio of the window
     *
     * @param heightRatio The ratio of the height of the window to the height of the primary monitor
     * @param aspectRatio The aspect ratio of the window
     */
    public static void init(float heightRatio, float aspectRatio) {
        if (initialized) {
            return;
        }

        Window.init(heightRatio, aspectRatio);
        init();
    }

    /**
     * Initialize the window.
     *
     * @param screenWidth  The window width in pixels.
     * @param screenHeight The window height in pixels.
     */
    public static void init(int screenWidth, int screenHeight) {
        if (initialized) {
            return;
        }

        Window.init(screenWidth, screenHeight);
        init();
    }

    private static void init() {
        Camera.init();
        Mouse.init();
        Scroll.init();
        Keyboard.init();
        Sound.init();
        ShaderProgram.init();

        initialized = true;
    }

    public static void update() {
        glfwPollEvents();
        glfwSwapBuffers(Window.getWindow());

        ResourceManager.freeResources();
        Clock.update();
        Camera.update();
    }
}
