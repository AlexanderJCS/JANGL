package jglt.coords;

import jglt.Consts;


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
        return dist / Consts.SCREEN_WIDTH * 2;
    }

    /**
     * Convert a pixel distance value on the Y axis to screen distance (which is what LWJGL uses).
     *
     * @param dist Distance
     * @return ScreenCoords distance (float)
     */
    public static float distYToScreenDist(float dist) {
        return dist / Consts.SCREEN_HEIGHT * 2;
    }

    /**
     * Convert pixel coordinates to screen coordinates.
     *
     * @return The ScreenCoords equivalent.
     */
    public ScreenCoords toScreenCoords() {
        float screenX = this.x / Consts.SCREEN_WIDTH * 2 - 1;
        float screenY = this.y / Consts.SCREEN_HEIGHT * 2 - 1;

        return new ScreenCoords(screenX, screenY);
    }
}
