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
     * Convert a pixel distance value on the Y axis to normalized device coordinates.
     *
     * @param dist Distance
     * @return WorldCoords distance (float)
     */
    public static float distToWorldCoords(float dist) {
        return dist / Window.getScreenHeight();
    }

    /**
     * Convert pixel coordinates to normalized device coordinates.
     *
     * @return The WorldCoords equivalent.
     */
    public WorldCoords toWorldCoords() {
        return new WorldCoords(distToWorldCoords(this.x), distToWorldCoords(this.y));
    }
}
