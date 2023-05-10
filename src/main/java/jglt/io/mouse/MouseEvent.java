package jglt.io.mouse;

import jglt.io.Event;

public class MouseEvent extends Event {
    /** The mouse button being pressed. */
    public final int button;

    /**
     * @param button The mouse button being pressed.
     * @param action The action performed. Either GLFW.GLFW_PRESS or GLFW.GLFW_RELEASE
     * @param mods Modifiers of the button press. I.e., if shift, control, etc. were pressed, this would be different
     */
    public MouseEvent(int button, int action, int mods) {
        super(action, mods);

        this.button = button;
    }

    @Override
    public String toString() {
        return "MouseEvent{" +
                "button=" + button +
                ", action=" + action +
                ", mods=" + mods +
                '}';
    }
}