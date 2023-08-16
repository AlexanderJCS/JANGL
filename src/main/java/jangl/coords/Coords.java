package jangl.coords;

import org.joml.Vector2f;

/**
 * The base coordinates class. See its inheritors: PixelCoords and WorldCoords
 */
public sealed abstract class Coords permits PixelCoords, WorldCoords {
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
     * A copy constructor for coords.
     * @param coords The coords object to deepcopy.
     */
    public Coords(Coords coords) {
        this.x = coords.x;
        this.y = coords.y;
    }

    /**
     * Copies the contents from a JOML Vector2f
     * @param vec The vector to copy
     */
    public Coords(Vector2f vec) {
        this.x = vec.x;
        this.y = vec.y;
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

    public Vector2f toVector2f() {
        return new Vector2f(this.x, this.y);
    }

    @Override
    public String toString() {
        return "X: " + this.x + " Y: " + this.y;
    }

    /**
     * Adds the value to this coordinate.
     *
     * @param coords The coordinates to add
     */
    public void add(WorldCoords coords) {
        this.x += coords.x;
        this.y += coords.y;
    }

    /**
     * Adds the value to this coordinate.
     *
     * @param x the x value to add to this x value
     * @param y the y value to add to this y value
     */
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Subtracts the value to from this coordinate
     *
     * @param coords The coordinates to subtract
     */
    public void sub(Coords coords) {
        this.x -= coords.x;
        this.y -= coords.y;
    }

    /**
     * Subtracts the value to from this coordinate
     *
     * @param x the x value to subtract from this x value
     * @param y the y value to subtract from this y value
     */
    public void sub(float x, float y) {
        this.x -= x;
        this.y -= y;
    }

    /**
     * Multiplies the x and y values by a certain amount.
     *
     * @param coords The amount to multiply
     */
    public void mul(Coords coords) {
        this.x *= coords.x;
        this.y *= coords.y;
    }

    /**
     * Multiplies the x and y values by a certain amount.
     *
     * @param x The x value to multiply to this x value.
     * @param y The y value to multiply to this y value.
     */
    public void mul(float x, float y) {
        this.x *= x;
        this.y *= y;
    }

    /**
     * Divides the x and y values by a certain amount.
     *
     * @param coords The amount to divide
     */
    public void div(Coords coords) {
        this.x /= coords.x;
        this.y /= coords.y;
    }

    /**
     * Divides the x and y values by a certain amount.
     *
     * @param x The x value to divide to this x value.
     * @param y The y value to divide to this y value.
     */
    public void div(float x, float y) {
        this.x /= x;
        this.y /= y;
    }
}
