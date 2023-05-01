package jglt.time;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * A utility class to handle different time-related tasks.
 */
public class Clock {
    /**
     * Second to last tick is used for getTimeDelta()
     */
    private static double secondToLastTick = 0;
    private static double lastTick = 0;

    /**
     * Busy waits for the necessary time for the given fps. Busy waiting means that the thread constantly checks
     * if the time is up, which uses more CPU power but can be very precise way of waiting.
     *
     * @param fps The fps the program should run at.
     */
    public static void busyTick(int fps) {
        // For future reference:
        // https://stackoverflow.com/questions/11498585/how-to-suspend-a-java-thread-for-a-small-period-of-time-like-100-nanoseconds

        double interval = 1.0 / fps;

        // Wait until the current time passed interval
        while (glfwGetTime() - lastTick < interval) {
        }

        secondToLastTick = lastTick;
        lastTick = glfwGetTime();
    }

    /**
     * @return The time between the last frame and the second to last frame.
     */
    public static double getTimeDelta() {
        return lastTick - secondToLastTick;
    }
}
