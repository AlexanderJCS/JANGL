package jangl.coords;

/**
 * The base coordinates class. See its inheritors: PixelCoords and ScreenCoords
 */
public sealed abstract class Coords permits PixelCoords, ScreenCoords {
    public float x;
    public float y;

    /**
     * @param x x coordinate
     * @param y y coordinate
     */
    public Coords(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }
}
