package jangl.coords;

import jangl.io.Window;

/**
 * ScreenCoords is the coordinate system that OpenGL uses to render to the screen. There are
 * two units on the X axis and two units on the Y axis for any screen size, meaning that one
 * unit in the X axis can be longer than one unit in the Y axis.
 */
public final class ScreenCoords extends Coords {
    public ScreenCoords(float x, float y) {
        super(x, y);
    }

    /**
     * @param coords A 2-length array of the 2D coordinates to be passed in
     * @throws IllegalArgumentException Throws if the array is not of length 2
     */
    public ScreenCoords(float[] coords) throws IllegalArgumentException {
        super(coords);
    }

    /**
     * Converts screen coords in the x direction to pixel coordinates.
     *
     * @param dist The screen coordinate distance
     * @return The pixel coordinate equivalent
     */
    public static float distXtoPixelCoords(float dist) {
        return dist * Window.getScreenWidth() / 2;
    }

    /**
     * Converts screen coords in the y direction to pixel coordinates.
     *
     * @param dist The screen coordinate distance
     * @return The pixel coordinate equivalent
     */
    public static float distYtoPixelCoords(float dist) {
        return dist * Window.getScreenHeight() / 2;
    }

    /**
     * Converts this object to pixel coordinates.
     *
     * @return The pixel coordinate equivalent
     */
    public PixelCoords toPixelCoords() {
        float pixCoordsX = (Window.getScreenWidth()) * (this.x + 1) / 2;
        float pixCoordsY = (Window.getScreenHeight()) * (this.y + 1) / 2;

        return new PixelCoords(pixCoordsX, pixCoordsY);
    }
}
