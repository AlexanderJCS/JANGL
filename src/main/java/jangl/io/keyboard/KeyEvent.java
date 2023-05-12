package jangl.io.keyboard;

import jangl.io.Event;

public class KeyEvent extends Event {
    /** The key that was pressed. */
    public final char key;
    /** The ID for a key that may not have a character value (for example, arrow keys) */
    public final int scancode;

    /**
     * @param key The key that was pressed.
     * @param scancode The ID for a key that may not have a character value (for example, arrow keys)
     * @param action The action performed.
     * @param mods Modifiers. Whether shift, alt, ctrl, caps lock, num lock, was pressed
     */
    public KeyEvent(char key, int scancode, int action, int mods) {
        super(action, mods);

        this.key = key;
        this.scancode = scancode;
    }

    @Override
    public String toString() {
        return "KeyEvent{" +
                "key=" + key +
                ", scancode=" + scancode +
                ", action=" + action +
                ", mods=" + mods +
                '}';
    }
}
