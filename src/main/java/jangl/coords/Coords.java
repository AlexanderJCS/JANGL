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

    /**
     * @param coords A 2-length array of the 2D coordinates to be passed in
     * @throws IllegalArgumentException Throws if the array is not of length 2
     */
    public Coords(float[] coords) throws IllegalArgumentException {
        if (coords.length != 2) {
            throw new IllegalArgumentException("Coordinates length must be equal to 2");
        }

        this.x = coords[0];
        this.y = coords[1];
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }
}
