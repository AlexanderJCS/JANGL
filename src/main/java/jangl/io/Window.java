package jangl.io;

import jangl.color.Color;
import jangl.graphics.TextureBuilder;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Window {
    public static int screenWidth;
    public static int screenHeight;
    private static Color clearColor;
    private static long window;
    private static boolean initialized = false;

    private Window() {}

    /**
     * Initialize the window. Vsync is off by default.
     * @param screenWidth The window width.
     * @param screenHeight The window height.
     */
    public static void init(int screenWidth, int screenHeight) {
        if (getInit()) {
            return;
        }

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW could not initialize");
        }

        Window.screenWidth = screenWidth;
        Window.screenHeight = screenHeight;

        initialized = true;

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);  // make the window non-resizeable
        window = glfwCreateWindow(screenWidth, screenHeight, "JANGL", 0, 0);
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);

        Window.setVsync(false);
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static boolean getInit() {
        return initialized;
    }

    public static long getWindow() {
        return window;
    }
    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT);  // Clear the screen for the next frame
    }

    public static void setClearColor(Color color) {
        clearColor = color;
        glClearColor(color.getNormRed(), color.getNormGreen(), color.getNormBlue(), color.getNormAlpha());
    }

    /**
     * @return An array of the background color, in RGBA, where for each value, 0 = no color and 1 = full color
     */
    public static Color getClearColor() {
        return clearColor;
    }

    public static boolean shouldRun() {
        return !glfwWindowShouldClose(getWindow());
    }

    /**
     * @param vsyncOn true to turn vsync on, false to turn vsync off.
     */
    public static void setVsync(boolean vsyncOn) {
        glfwSwapInterval(vsyncOn ? 1 : 0);
    }

    /**
     * By default, wireframe mode is off.
     *
     * @param showWireframe True to show the wireframes of objects. False to not set the wireframes.
     */
    public static void showWireframe(boolean showWireframe) {
        glPolygonMode(GL_FRONT_AND_BACK, showWireframe ? GL_LINE : GL_FILL);
    }

    public static void close() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public static void setTitle(String newTitle) {
        glfwSetWindowTitle(getWindow(), newTitle);
    }

    /**
     * Sets the taskbar icon of the application.
     * @param builder The TextureBuilder of the texture to display
     */
    public static void setIcon(TextureBuilder builder) {
        glfwSetWindowIcon(Window.getWindow(), builder.toGLFWImageBuffer());
    }
}
