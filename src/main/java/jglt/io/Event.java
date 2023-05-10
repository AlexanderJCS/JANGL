package jglt.io;

public class Event {
    /** The action performed. Either GLFW.GLFW_PRESS or GLFW.GLFW_RELEASE */
    public final int action;
    /** Modifiers of the button press. i.e., if shift, control, etc. were pressed, this would be different */
    public final int mods;

    /**
     * @param action The action performed. Either GLFW.GLFW_PRESS or GLFW.GLFW_RELEASE
     * @param mods Modifiers of the button press. I.e., if shift, control, etc. were pressed, this would be different
     */
    public Event(int action, int mods) {
        this.action = action;
        this.mods = mods;
    }
}
