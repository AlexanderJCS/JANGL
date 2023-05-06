package jglt.shapes;

import jglt.coords.ScreenCoords;
import jglt.graphics.texture.TexturedModel;

/**
 * The "base" of every other rectangular object. Used for collision and conversion to a Model or TexturedModel class.
 */
public class Rect implements Shape, AutoCloseable {
    private final TexturedModel model;
    public float x1, y1, x2, y2;
    public float width, height;

    /**
     * @param coords the coordinates of the rect
     * @param width  Width
     * @param height Height
     */
    public Rect(ScreenCoords coords, float width, float height) {
        this.x1 = coords.x;
        this.y1 = coords.y;
        this.x2 = coords.x + width;
        this.y2 = coords.y + height;

        this.width = width;
        this.height = height;

        this.model = this.toTexturedModel();
    }

    public void setWidthHeight(float width, float height) {
        this.width = width;
        this.height = height;

        this.setPos(new ScreenCoords(this.x1, this.y1));
    }

    /**
     * Get the center coordinates of the rect.
     *
     * @return The center coordinates of the rect, type ScreenCoords
     */
    @Override
    public ScreenCoords getCenter() {
        return new ScreenCoords(
                (this.x1 + this.x2) / 2,
                (this.y1 + this.y2) / 2
        );
    }

    /**
     * Set the center of the rect.
     *
     * @param x The x coordinate of the rect center.
     * @param y The y coordinate of the rect center.
     */
    public void setCenter(float x, float y) {
        float halfWidth = (x2 - x1) / 2;
        float halfHeight = (y2 - y1) / 2;

        this.x1 = x - halfWidth;
        this.x2 = x + halfWidth;
        this.y1 = y - halfHeight;
        this.y2 = y + halfHeight;
    }

    /**
     * Sets the coordinates to the ones given.
     *
     * @param coords The new coordinates
     */
    public void setPos(ScreenCoords coords) {
        this.x1 = coords.x;
        this.y1 = coords.y;
        this.x2 = this.x1 + width;
        this.y2 = this.y1 + height;

        this.model.changeVertices(this.getVertices());
    }

    /**
     * Shift the rectangle by a certain amount
     *
     * @param x X amount to shift
     * @param y Y amount to shift
     */
    @Override
    public void shift(float x, float y) {
        this.x1 += x;
        this.x2 += x;
        this.y1 += y;
        this.y2 += y;

        this.model.changeVertices(this.getVertices());
    }

    @Override
    public float[] getVertices() {
        return new float[] {
                x1, y1,  // Top left
                x2, y1,  // Top right
                x2, y2,  // Bottom right
                x1, y2,  // Bottom left
        };
    }

    public int[] getIndices() {
        return new int[] {
                0, 1, 2,
                2, 3, 0
        };
    }

    @Override
    public void draw() {
        this.model.render();
    }

    /**
     * Converts the vertices of the rect into a textured model.
     *
     * @return A textured model.
     */
    private TexturedModel toTexturedModel() {
        float[] texCoords = new float[] {
                0, 1,
                1, 1,
                1, 0,
                0, 0,
        };

        return new TexturedModel(this.getVertices(), this.getIndices(), texCoords);
    }

    /**
     * Check if a point is inside the rect. Used for collision checking.
     *
     * @param coords The point
     * @return If it is inside the rect
     */
    public boolean pointInsideRect(ScreenCoords coords) {
        return (coords.y > this.y1 && coords.y < this.y2) &&
                (coords.x > this.x1 && coords.x < this.x2);
    }

    /**
     * Check if the other rect's vertices are inside this rect. NOTE that just because the other rect's vertices are
     * inside this rect, that doesn't mean that this rect's vertices are inside the other rect. This means that this
     * method should always be called as:
     * <p>
     * largerRect.collidesWith(smallerRect)
     * <p>
     * If largerRect and smallerRect were reversed, collision would only occur when smallerRect touches largerRect's
     * vertices.
     *
     * @param other The other rect
     * @return If other is inside this rect
     */
    public boolean otherRectInside(Rect other) {
        return this.pointInsideRect(new ScreenCoords(other.x1, other.y1)) ||   // Top left corner
                this.pointInsideRect(new ScreenCoords(other.x1, other.y2)) ||  // Bottom left corner
                this.pointInsideRect(new ScreenCoords(other.x2, other.y1)) ||  // Top right corner
                this.pointInsideRect(new ScreenCoords(other.x2, other.y2));    // Bottom right corner
    }

    @Override
    public boolean collidesWith(Rect other) {
        return this.otherRectInside(other) || other.otherRectInside(this);
    }

    @Override
    public boolean collidesWith(Circle other) {
        return other.collidesWith(this);
    }

    @Override
    public String toString() {
        return "Rect{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    /**
     * The close model needs to be closed at the end of a Rect's usage to prevent a memory leak
     */
    @Override
    public void close() {
        this.model.close();
    }
}
