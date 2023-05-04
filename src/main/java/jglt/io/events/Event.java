package jglt.io.events;

public record Event(char key, int scancode, int action, int mods) {
    @Override
    public String toString() {
        return "Event{" +
                "key=" + key +
                ", scancode=" + scancode +
                ", action=" + action +
                ", mods=" + mods +
                '}';
    }
}
