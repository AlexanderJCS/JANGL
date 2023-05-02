package jglt.io.events;

import org.lwjgl.glfw.GLFWKeyCallbackI;

public class EventCallback implements GLFWKeyCallbackI {
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        Event event = new Event(key, scancode, action, mods);
        Events.addEvent(event);
    }
}
