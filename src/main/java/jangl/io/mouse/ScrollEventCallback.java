package jangl.io.mouse;

import org.lwjgl.glfw.GLFWScrollCallbackI;

public class ScrollEventCallback implements GLFWScrollCallbackI {
    ScrollEventCallback() {
    }

    @Override
    public void invoke(long window, double xOffset, double yOffset) {
        Scroll.addEvent(new ScrollEvent(xOffset, yOffset));
    }
}
