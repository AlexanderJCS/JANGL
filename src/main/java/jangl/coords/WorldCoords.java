package jangl.coords;

import jangl.io.Window;

public final class WorldCoords extends Coords {
    public WorldCoords(float x, float y) {
        super(x, y);
    }

    /**
     * @param coords A 2-length array of the 2D coordinates to be passed in
     * @throws IllegalArgumentException Throws if the array is not of length 2
     */
    public WorldCoords(float[] coords) throws IllegalArgumentException {
        super(coords);
    }

    /**
     * Converts a distance in the units of world coords to pixel coords.
     *
     * @param dist The WorldCoords distance
     * @return The pixel coordinate equivalent
     */
    public static float distToPixelCoords(float dist) {
        return dist * Window.getScreenHeight();
    }

    /**
     * Converts this object to pixel coordinates.
     *
     * @return The pixel coordinate equivalent
     */
    public PixelCoords toPixelCoords() {
        return new PixelCoords(distToPixelCoords(this.x), distToPixelCoords(this.y));
    }

    /**
     * @return The middle of the screen, assuming that the camera is not moved in any direction.
     */
    public static WorldCoords getMiddle() {
        return new WorldCoords((float) Window.getScreenWidth() / Window.getScreenHeight() / 2f, 0.5f);
    }
}
