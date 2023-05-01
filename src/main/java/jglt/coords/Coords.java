package jglt.coords;

public abstract class Coords {
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
