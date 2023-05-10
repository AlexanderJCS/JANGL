package jglt.io.mouse;

/**
 * @param button The mouse button being pressed.
 * @param action The action performed. Either GLFW.GLFW_PRESS or GLFW.GLFW_RELEASE
 * @param mods Modifiers of the button press. I.e., if shift, control, etc. were pressed, this would be different
 */
public record MouseEvent(int button, int action, int mods) {}