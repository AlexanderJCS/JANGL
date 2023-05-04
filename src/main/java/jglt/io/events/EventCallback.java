package jglt.io.events;

import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * Creates new events and adds them to the Events class.
 */
public class EventCallback implements GLFWKeyCallbackI {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        Event event = new Event((char) key, scancode, action, mods);
        Events.addEvent(event);
    }
}
