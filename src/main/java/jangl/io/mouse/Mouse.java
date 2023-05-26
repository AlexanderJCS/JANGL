package jangl.io.mouse;

import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.io.EventsConfig;
import jangl.io.Window;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
    private static final DoubleBuffer MOUSE_POS_BUFFER_X = BufferUtils.createDoubleBuffer(1);
    private static final DoubleBuffer MOUSE_POS_BUFFER_Y = BufferUtils.createDoubleBuffer(1);

    private static final List<MouseEvent> EVENTS = new ArrayList<>();
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
        glfwGetCursorPos(Window.getWindow(), MOUSE_POS_BUFFER_X, MOUSE_POS_BUFFER_Y);
        double x = MOUSE_POS_BUFFER_X.get(0);
        double y = MOUSE_POS_BUFFER_Y.get(0);

        return new PixelCoords((float) x, (float) (Window.getScreenHeight() - y)).toScreenCoords();
    }

    /**
     * @param button the mouse button, input is GLFW.GLFW_MOUSE_BUTTON_LEFT or GLFW.GLFW_MOUSE_BUTTON_RIGHT enum.
     * @return returns true if the mouse is down.
     */
    public static boolean mouseDown(int button) {
        return glfwGetMouseButton(Window.getWindow(), button) == GLFW_PRESS;
    }

    static void addEvent(MouseEvent event) {
        EVENTS.add(event);

        // Remove old events if the events list size is too large
        if (EVENTS.size() > EventsConfig.maxEvents) {
            EVENTS.remove(0);
        }
    }

    public static List<MouseEvent> getEvents() {
        List<MouseEvent> eventsDeepcopy = new ArrayList<>(EVENTS);
        EVENTS.clear();

        return eventsDeepcopy;
    }
}
