package jglt.io.events;

/**
 * @param key The key that was pressed.
 * @param scancode The ID for a key that may not have a character value (for example, arrow keys)
 * @param action The action performed.
 * @param mods Modifiers. Whether shift, alt, ctrl, caps lock, num lock, was pressed
 */
public record Event(char key, int scancode, int action, int mods) {}
