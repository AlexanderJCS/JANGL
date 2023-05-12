package jangl.shapes;

import jangl.coords.ScreenCoords;
import jangl.graphics.models.TexturedModel;

/**
 * The "base" of every other rectangular object. Used for collision and conversion to a Model or TexturedModel class.
 */
public class Rect extends Shape implements AutoCloseable {
    public float x1, y1, x2, y2;
    public float width, height;

    /**
     * @param coords the coordinates of the rect
     * @param width  Width
     * @param height Height
     */
    public Rect(ScreenCoords coords, float width, float height) {
        this.axisAngle = 0;

        this.x1 = coords.x;
        this.y1 = coords.y;
        this.x2 = coords.x + width;
        this.y2 = coords.y - height;

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
        this.y2 = this.y1 - height;

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
        return Shape.rotateAxis(
                new float[] {
                    x1, y1,  // Top left
                    x2, y1,  // Top right
                    x2, y2,  // Bottom right
                    x1, y2,  // Bottom left
                },
                this.axisAngle
        );
    }

    @Override
    public float[] getExteriorVertices() {
        return this.getVertices();
    }

    /**
     * @return The vertex indices that are passed to OpenGL.
     */
    private int[] getIndices() {
        return new int[] {
                0, 1, 2,
                2, 3, 0
        };
    }

    /**
     * Draws the rectangle to the screen. If you want to display a texture, bind the texture before
     * calling this method.
     */
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
                0, 0,
                1, 0,
                1, 1,
                0, 1,
        };

        return new TexturedModel(this.getVertices(), this.getIndices(), texCoords);
    }

    /**
     * The close model needs to be closed at the end of a Rect's usage to prevent a memory leak
     */
    @Override
    public void close() {
        this.model.close();
    }
}