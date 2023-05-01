package jglt.io;

import jglt.Consts;
import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.BufferManager;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static boolean mouseDownLastFrame;
    private static boolean initialized = false;

    public static void init() {
        if (getInit()) {
            return;
        }

        initialized = true;
        Mouse.mouseDownLastFrame = false;
    }

    public static boolean getInit() {
        return initialized;
    }

    /**
     * This method should be called <i>only</i> as the last update method in the game loop.
     * Otherwise, Mouse.mouseDownThisFrame() will not function correctly.
     */
    public static void update() {
        mouseDownLastFrame = mouseDown(GLFW_MOUSE_BUTTON_LEFT);
    }

    /**
     * Returns the mouse position in the units of ScreenCoords. It's also important to note that the coordinates
     * may be offscreen if the mouse is offscreen.
     *
     * @return The mouse position in the units of ScreenCoords.
     */
    public static ScreenCoords getMousePos() {
        // https://stackoverflow.com/questions/33592499/lwjgl-3-get-cursor-position
        glfwGetCursorPos(Window.getWindow(), BufferManager.mouseBuffer1, BufferManager.mouseBuffer2);
        double x = BufferManager.mouseBuffer1.get(0);
        double y = BufferManager.mouseBuffer2.get(0);

        return new PixelCoords((float) x, (float) (Consts.SCREEN_HEIGHT - y)).toScreenCoords();
    }

    /**
     * @param button the mouse button, input is GLFW.GLFW_MOUSE_BUTTON_LEFT or GLFW.GLFW_MOUSE_BUTTON_RIGHT enum.
     * @return returns true if the mouse is down.
     */
    public static boolean mouseDown(int button) {
        return glfwGetMouseButton(Window.getWindow(), button) == GLFW_PRESS;
    }

    /**
     * Requires the update Mouse.update() method to be called in order to function correctly. Note that
     * Mouse.update() needs to be called as the last update method in the game loop, otherwise this
     * method will always return false.
     *
     * @return True if the left mouse button has been pressed this frame.
     * False if it has been held down for more than one frame.
     */
    public static boolean mouseDownThisFrame() {
        return (!mouseDownLastFrame && mouseDown(GLFW_MOUSE_BUTTON_LEFT));
    }
}
