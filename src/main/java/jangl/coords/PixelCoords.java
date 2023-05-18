package jangl.coords;


import jangl.io.Window;

/**
 * Pixel coordinates with the origin (0, 0) at the bottom left.
 */
public final class PixelCoords extends Coords {
    public PixelCoords(float x, float y) {
        super(x, y);
    }

    /**
     * @param coords A 2-length array of the 2D coordinates to be passed in
     * @throws IllegalArgumentException Throws if the array is not of length 2
     */
    public PixelCoords(float[] coords) throws IllegalArgumentException {
        super(coords);
    }

    /**
     * Convert a pixel distance value on the X axis to screen distance.
     *
     * @param dist Distance
     * @return ScreenCoords distance (float)
     */
    public static float distXtoScreenDist(float dist) {
        return dist / Window.getScreenWidth() * 2;
    }

    /**
     * Convert a pixel distance value on the Y axis to screen distance.
     *
     * @param dist Distance
     * @return ScreenCoords distance (float)
     */
    public static float distYtoScreenDist(float dist) {
        return dist / Window.getScreenHeight() * 2;
    }

    /**
     * Convert pixel coordinates to screen coordinates.
     *
     * @return The ScreenCoords equivalent.
     */
    public ScreenCoords toScreenCoords() {
        float screenX = this.x / Window.getScreenWidth() * 2 - 1;
        float screenY = this.y / Window.getScreenHeight() * 2 - 1;

        return new ScreenCoords(screenX, screenY);
    }
}
