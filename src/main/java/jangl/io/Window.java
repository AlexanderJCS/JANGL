package jangl.io;

import jangl.color.Color;
import jangl.graphics.textures.TextureBuilder;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glGetError;
import static org.lwjgl.opengl.GL41.*;

public class Window {
    public static int screenWidth;
    public static int screenHeight;
    private static Color clearColor;
    private static long window;
    private static boolean initialized = false;
    private static boolean glfwIsInitialized = false;
    private static long currentCursor = -1;

    private Window() {
    }

    /**
     * Initializes the window using two ratios:
     * a. the ratio of the window height to the primary monitor height
     * b. the aspect ratio of the window
     * @param heightRatio The ratio of the height of the window to the height of the primary monitor
     * @param aspectRatio The aspect ratio of the window
     */
    public static void init(float heightRatio, float aspectRatio) {
        if (getInit()) {
            return;
        }

        if (!glfwIsInitialized && !glfwInit()) {
            throw new IllegalStateException("GLFW could not initialize");
        }

        glfwIsInitialized = true;

        float windowHeight = heightRatio * getMonitorHeight();
        float windowWidth = windowHeight * aspectRatio;

        init((int) windowWidth, (int) windowHeight);
    }

    /**
     * Initialize the window. Vsync is off by default.
     *
     * @param screenWidth  The window width.
     * @param screenHeight The window height.
     */
    public static void init(int screenWidth, int screenHeight) {
        if (getInit()) {
            return;
        }

        if (!glfwIsInitialized && !glfwInit()) {
            throw new IllegalStateException("GLFW could not initialize");
        }

        glfwIsInitialized = true;

        Window.screenWidth = screenWidth;
        Window.screenHeight = screenHeight;

        initialized = true;

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

        // JANGL supports OpenGL v4.1
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);

        window = glfwCreateWindow(screenWidth, screenHeight, "JANGL", 0, 0);
        glfwShowWindow(window);

        glfwSetWindowPos(
                window,
                (getMonitorWidth() - getScreenWidth()) / 2,
                (getMonitorHeight() - getScreenHeight()) / 2
        );

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        // This must be enabled to make transparency work properly
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Window.setVsync(false);
    }

    /**
     * @return the height of the primary monitor in pixels.
     */
    public static int getMonitorHeight() {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (mode == null) {
            throw new IllegalStateException("Could not get monitor height. Is JANGL initialized?");
        }

        return mode.height();
    }

    /**
     * @return the width of the primary monitor in pixels.
     */
    public static int getMonitorWidth() {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (mode == null) {
            throw new IllegalStateException("Could not get monitor width. Is JANGL initialized?");
        }

        return mode.width();
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
     * @return An array of the background color, in RGBA, where for each value, 0 = no color and 1 = full color
     */
    public static Color getClearColor() {
        return clearColor;
    }

    public static void setClearColor(Color color) {
        clearColor = color;
        glClearColor(color.getNormRed(), color.getNormGreen(), color.getNormBlue(), color.getNormAlpha());
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
     *
     * @param builder The TextureBuilder of the texture to display
     */
    public static void setIcon(TextureBuilder builder) {
        glfwSetWindowIcon(Window.getWindow(), builder.toGLFWImageBuffer());
    }

    public static void setCursor(TextureBuilder builder) {
        if (currentCursor != -1) {
            glfwDestroyCursor(currentCursor);
        }

        currentCursor = glfwCreateCursor(builder.toGLFWImage(), 0, 0);

        glfwSetCursor(Window.getWindow(), currentCursor);
    }
}
