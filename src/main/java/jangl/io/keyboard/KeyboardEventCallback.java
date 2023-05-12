package jangl.io.keyboard;

import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Creates new events and adds them to the Events class.
 */
public class KeyboardEventCallback implements GLFWKeyCallbackI {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        KeyEvent event = new KeyEvent((char) key, scancode, action, mods);
        Keyboard.addEvent(event);
    }
}
