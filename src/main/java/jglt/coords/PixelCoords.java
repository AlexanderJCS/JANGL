package jglt.coords;


import jglt.io.Window;

/**
 * Pixel coordinates with the origin (0, 0) at the top left.
 */
public class PixelCoords extends Coords {
    public PixelCoords(float x, float y) {
        super(x, y);
    }

    /**
     * Convert a pixel distance value on the X axis to screen distance (which is what LWJGL uses).
     *
     * @param dist Distance
     * @return ScreenCoords distance (float)
     */
    public static float distXToScreenDist(float dist) {
        return dist / Window.getScreenWidth() * 2;
    }

    /**
     * Convert a pixel distance value on the Y axis to screen distance (which is what LWJGL uses).
     *
     * @param dist Distance
     * @return ScreenCoords distance (float)
     */
    public static float distYToScreenDist(float dist) {
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

    /**
     * @param other The other point to find the distance between
     * @return The distance, in pixels, between this and other
     */
    public double dist(PixelCoords other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}
