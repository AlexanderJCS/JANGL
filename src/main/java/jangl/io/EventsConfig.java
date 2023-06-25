package jangl.io;

public class EventsConfig {
    /**
     * The maximum number of events that can be stored in Mouse or Keyboard before it removes old events. This is
     * to prevent too much memory consumption by never getting events from Mouse or Keyboard.
     */
    public static final int MAX_EVENTS = 1024;
}
