package jangl.io.mouse;

/**
 * An event for when the mouse is scrolled. This does not extend Event since GLFW does not provide action or modifier
 * information.
 */
public class ScrollEvent {
    /*
     * Note for the future: do not make this class a record, since it will not be consistent with MouseEvent and
     * KeyEvent when accessing variables.
     */
    public final double xOffset;
    public final double yOffset;

    public ScrollEvent(double xOffset, double yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public String toString() {
        return "ScrollEvent{" +
                "xOffset=" + xOffset +
                ", yOffset=" + yOffset +
                '}';
    }
}
