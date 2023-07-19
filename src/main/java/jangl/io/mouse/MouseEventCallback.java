package jangl.io.mouse;

import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public class MouseEventCallback implements GLFWMouseButtonCallbackI {
    MouseEventCallback() {
    }

    @Override
    public void invoke(long window, int button, int action, int mods) {
        Mouse.addEvent(new MouseEvent(button, action, mods));
    }
}
