package jangl.time;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

/**
 * A utility class to handle different time-related tasks.
 */
public final class Clock {
    /**
     * Second to last tick is used for getTimeDelta()
     */
    private static double secondToLastTick = 0;
    private static double lastTick = 0;
    private static double[] fpsSamples = new double[100];
    /** The next index to modify in fpsSamples. */
    private static int fpsSampleIndex = 0;

    private Clock() {}


    /**
     * Busy waits for the necessary time for the given fps. Busy waiting means that the thread constantly checks
     * if the time is up, which uses more CPU power but can be very precise way of waiting.
     *
     * @param fps The fps the program should run at.
     */
    public static void busyTick(double fps) {
        // For future reference:
        // https://stackoverflow.com/questions/11498585/how-to-suspend-a-java-thread-for-a-small-period-of-time-like-100-nanoseconds

        double interval = 1 / fps;

        // Wait until the current time passed interval
        while (glfwGetTime() - lastTick < interval) {}

        secondToLastTick = lastTick;
        lastTick = glfwGetTime();

        fpsSamples[fpsSampleIndex] = 1 / getTimeDelta();
        fpsSampleIndex++;

        if (fpsSampleIndex >= fpsSamples.length) {
            fpsSampleIndex = 0;
        }
    }

    /**
     * @return The time between the last frame and the second to last frame.
     */
    public static double getTimeDelta() {
        return lastTick - secondToLastTick;
    }

    /**
     * @param n The number of samples to set the smoothed FPS to. Must be >= 1.
     *          Warning: This will need to restart FPS sample collection, so the smoothed FPS won't be valid for
     *          n frames.
     */
    public static void setNumFpsSamples(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("Smoothed FPS sample size must be 1 or greater, not " + n + ".");
        }

        fpsSamples = new double[n];
    }

    /**
     * @return The number of smoothed FPS samples
     */
    public static int getNumFpsSamples() {
        return fpsSamples.length;
    }

    /**
     * @return The smoothed fps over the last n samples
     */
    public static double getSmoothedFps() {
        double sum = 0;

        for (double sample : fpsSamples) {
            sum += sample;
        }

        return sum / fpsSamples.length;
    }
}
