package jangl.io;

import org.lwjgl.opengl.GL;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Window {
    public static int screenWidth;
    public static int screenHeight;
    private static float[] clearColor;
    private static long window;
    private static boolean initialized = false;

    private Window() {}

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

    /**
     * @param red   The red value of the background, float between 0 and 1
     * @param green The green value of the background, float between 0 and 1
     * @param blue  The blue value of the background, float between 0 and 1
     * @param alpha The alpha value of the background, float between 0 and 1
     */
    public static void setClearColor(float red, float green, float blue, float alpha) {
        clearColor = new float[]{ red, green, blue, alpha };
        glClearColor(red, green, blue, alpha);
    }

    /**
     * @param rgba An RGBA float array of the background color. For each float, 0 = no color and 1 = full color.
     *
     * @throws IllegalArgumentException Throws if the array is not of length 4 (one value for R, G, B, and A)
     */
    public static void setClearColor(float[] rgba) throws IllegalArgumentException {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA float array must be of length 4: red, green, blue, alpha");
        }

        setClearColor(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    /**
     * @return An array of the background color, in RGBA, where for each value, 0 = no color and 1 = full color
     */
    public static float[] getClearColor() {
        return Arrays.copyOf(clearColor, clearColor.length);
    }

    public static boolean shouldRun() {
        return !glfwWindowShouldClose(getWindow());
    }

    public static void close() {
        glfwTerminate();
    }

    public static void setTitle(String newTitle) {
        glfwSetWindowTitle(getWindow(), newTitle);
    }
}
