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
    /**
     * The next index to modify in fpsSamples.
     */
    private static int fpsSampleIndex = 0;

    private Clock() {}


    /**
     * Busy waits for the necessary time for the given fps. Busy waiting means that the thread constantly checks
     * if the time is up, which uses more CPU power but can be very precise way of waiting.
     *
     * @param fps The frames per second the program should run at.
     */
    public static void busyTick(double fps) {
        // For future reference:
        // https://stackoverflow.com/questions/11498585/how-to-suspend-a-java-thread-for-a-small-period-of-time-like-100-nanoseconds

        double interval = 1 / fps;

        // Wait until the current time passed interval
        while (glfwGetTime() - lastTick < interval);
    }

    /**
     * The smartTick method differs from busyTick in that it provides a CPU-efficient way to wait for the next frame.
     * This is generally always recommended over the busyTick alternative.
     * <p>
     * To learn more about this, <a href="https://blog.bearcats.nl/accurate-sleep-function/">visit this blog post</a>
     *
     * @param fps The frames per second the program should run at.
     * @throws InterruptedException Throws if the thread is interrupted while sleeping.
     */
    public static void smartTick(double fps) throws InterruptedException {
        double seconds = 1 / fps - lastTick;

        double estimate = 5e-3;
        double mean = 5e-3;
        double m2 = 0;
        long count = 1;

        while (seconds > estimate) {
            double start = glfwGetTime();
            Thread.sleep(1);
            double end = glfwGetTime();

            double observed = end - start;
            seconds -= observed;

            count++;

            double delta = observed - mean;
            mean += delta / count;
            m2 += delta * (observed - mean);
            double stdDev = Math.sqrt(m2 / (count - 1));

            estimate = mean + stdDev;
        }

        // Busy wait
        double start = glfwGetTime();
        while (glfwGetTime() - start < seconds);
    }

    /**
     * Sets deltaTime to the correct value
     */
    private static void updateDeltaTime() {
        secondToLastTick = lastTick;
        lastTick = glfwGetTime();
    }

    /**
     * Add a new value to smoothedFps. Should only be called after updateDeltaTime so the information is for this frame
     * and not the previous frame.
     */
    private static void updateSmoothedFPS() {
        fpsSamples[fpsSampleIndex] = 1 / getTimeDelta();
        fpsSampleIndex++;

        if (fpsSampleIndex >= fpsSamples.length) {
            fpsSampleIndex = 0;
        }
    }

    /**
     * Updates the delta time and the smoothed FPS.
     */
    public static void update() {
        updateDeltaTime();
        updateSmoothedFPS();
    }

    /**
     * @return The time between the last frame and the second to last frame.
     */
    public static double getTimeDelta() {
        return lastTick - secondToLastTick;
    }

    /**
     * @return The number of smoothed FPS samples
     */
    public static int getNumFpsSamples() {
        return fpsSamples.length;
    }

    /**
     * @param n The number of samples to set the smoothed FPS to. Must be >= 1.
     */
    public static void setNumFpsSamples(int n) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("Smoothed FPS sample size must be 1 or greater, not " + n + ".");
        }

        fpsSamples = new double[n];
    }

    /**
     * @return The smoothed fps over the last n samples
     */
    public static double getSmoothedFps() {
        int divide = fpsSamples.length;
        double sum = 0;

        for (double sample : fpsSamples) {
            sum += sample;

            // Prevent smoothed fps having to "climb up"
            if (sample == 0) {
                divide--;
            }
        }

        return sum / divide;
    }

    public static double getNonSmoothedFPS() {
        return 1 / Clock.getTimeDelta();
    }
}
