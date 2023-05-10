package jglt.io.mouse;

import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.BufferManager;
import jglt.io.EventContainer;
import jglt.io.Window;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse extends EventContainer {
    private static boolean initialized = false;
    private Mouse() {
    }

    public static void init() {
        if (getInit()) {
            return;
        }

        glfwSetMouseButtonCallback(Window.getWindow(), new MouseEventCallback());
        initialized = true;
    }

    public static boolean getInit() {
        return initialized;
    }

    /**
     * Returns the mouse position in the units of ScreenCoords. It's also important to note that the coordinates
     * may be offscreen if the mouse is offscreen.
     *
     * @return The mouse position in the units of ScreenCoords.
     */
    public static ScreenCoords getMousePos() {
        // https://stackoverflow.com/questions/33592499/lwjgl-3-get-cursor-position
        glfwGetCursorPos(Window.getWindow(), BufferManager.MOUSE_BUFFER_1, BufferManager.MOUSE_BUFFER_2);
        double x = BufferManager.MOUSE_BUFFER_1.get(0);
        double y = BufferManager.MOUSE_BUFFER_2.get(0);

        return new PixelCoords((float) x, (float) (Window.getScreenHeight() - y)).toScreenCoords();
    }

    /**
     * @param button the mouse button, input is GLFW.GLFW_MOUSE_BUTTON_LEFT or GLFW.GLFW_MOUSE_BUTTON_RIGHT enum.
     * @return returns true if the mouse is down.
     */
    public static boolean mouseDown(int button) {
        return glfwGetMouseButton(Window.getWindow(), button) == GLFW_PRESS;
    }
}
