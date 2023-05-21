package jangl.io;

import jangl.coords.ScreenCoords;
import jangl.graphics.shaders.ColorShader;
import jangl.shapes.Rect;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

public class Window {
    public static int screenWidth;
    public static int screenHeight;

    private static ColorShader BG_COLOR;
    private static Rect BG_RECT;
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

        // Create the background once everything else is done to avoid an OpenGL access violation exception
        BG_COLOR = new ColorShader(0, 0, 0, 1);
        BG_RECT = new Rect(new ScreenCoords(-1, 1), 2, 2);
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

    private static boolean backgroundNotBlack() {
        float[] bgColor = BG_COLOR.getRGBA();

        return bgColor[0] != 0 || bgColor[1] != 0 || bgColor[2] != 0;
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT);  // Clear the screen for the next frame

        if (backgroundNotBlack()) {
            BG_RECT.draw(BG_COLOR);
        }
    }

    /**
     * @param red   The red value of the background, float between 0 and 1
     * @param green The green value of the background, float between 0 and 1
     * @param blue  The blue value of the background, float between 0 and 1
     * @param alpha The alpha value of the background, float between 0 and 1
     */
    public static void setBackgroundColor(float red, float green, float blue, float alpha) {
        BG_COLOR.setRGBA(red, green, blue, alpha);
    }

    /**
     * @param rgba An RGBA float array of the background color. For each float, 0 = no color and 1 = full color.
     *
     * @throws IllegalArgumentException Throws if the array is not of length 4 (one value for R, G, B, and A)
     */
    public static void setBackgroundColor(float[] rgba) throws IllegalArgumentException {
        BG_COLOR.setRGBA(rgba);
    }

    /**
     * @return An array of the background color, in RGBA, where for each value, 0 = no color and 1 = full color
     */
    public static float[] getBackgroundColor() {
        return BG_COLOR.getRGBA();
    }

    public static boolean shouldRun() {
        return !glfwWindowShouldClose(getWindow());
    }

    public static void close() {
        glfwTerminate();

        BG_COLOR.close();
        BG_RECT.close();
    }

    public static void setTitle(String newTitle) {
        glfwSetWindowTitle(getWindow(), newTitle);
    }
}
