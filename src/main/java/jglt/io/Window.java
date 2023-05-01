package jglt.io;

import jglt.Consts;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private static long window;
    private static boolean initialized = false;

    public static void init() {
        if (getInit()) {
            return;
        }

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW could not initialize");
        }

        initialized = true;

        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);  // make the window non-resizeable
        window = glfwCreateWindow(Consts.SCREEN_WIDTH, Consts.SCREEN_HEIGHT, "Dungeon Crawler", 0, 0);
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static boolean getInit() {
        return initialized;
    }

    public static long getWindow() {
        return window;
    }
}
