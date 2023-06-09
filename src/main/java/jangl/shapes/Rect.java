package jangl.shapes;

import jangl.coords.NDCoords;
import jangl.graphics.models.TexturedModel;

/**
 * The "base" of every other rectangular object. Used for collision and conversion to a Model or TexturedModel class.
 */
public class Rect extends Shape {
    private float texRepeatY, texRepeatX;
    private float x1, y1, x2, y2;
    private float width, height;

    /**
     * @param topLeft the top left coordinate of the rect
     * @param width   The width of the rect, units of normalized device coordinates
     * @param height  The height of the rect, units of normalized device coordinates
     */
    public Rect(NDCoords topLeft, float width, float height) {
        this.x1 = topLeft.x;
        this.y1 = topLeft.y;
        this.x2 = topLeft.x + width;
        this.y2 = topLeft.y - height;

        this.texRepeatY = 1;
        this.texRepeatX = 1;

        this.width = width;
        this.height = height;

        this.model = this.toTexturedModel();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidthHeight(float width, float height) {
        this.width = width;
        this.height = height;

        this.setPos(new NDCoords(this.x1, this.y1));
    }

    /**
     * Get the center coordinates of the rect.
     *
     * @return The center coordinates of the rect, type NDCoords
     */
    @Override
    public NDCoords getCenter() {
        return new NDCoords(
                Shape.rotateAxis(
                        new float[]{
                                (this.x1 + this.x2) / 2,
                                (this.y1 + this.y2) / 2
                        },
                        this.axisAngle
                )
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
    public void setPos(NDCoords coords) {
        this.x1 = coords.x;
        this.y1 = coords.y;
        this.x2 = this.x1 + width;
        this.y2 = this.y1 - height;

        this.model.subVertices(this.calculateVertices(), 0);
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

        this.model.subVertices(this.calculateVertices(), 0);
    }

    @Override
    public float[] calculateVertices() {
        return Shape.rotateAxis(
                Shape.rotateLocal(
                        new float[]{
                                x1, y1,  // Top left
                                x2, y1,  // Top right
                                x2, y2,  // Bottom right
                                x1, y2,  // Bottom left
                        },
                        this.getCenter(),
                        this.localAngle
                ),
                this.axisAngle
        );
    }

    @Override
    public float[] getExteriorVertices() {
        return this.calculateVertices();
    }

    /**
     * @return The vertex indices that are passed to OpenGL.
     */
    protected int[] getIndices() {
        return new int[]{
                0, 1, 2,
                2, 3, 0
        };
    }

    /**
     * @param repeatTimes The amount of times for the texture to repeat over the x-axis.
     */
    public void setTexRepeatY(float repeatTimes) {
        this.texRepeatY = repeatTimes;
        this.model = this.toTexturedModel();
    }

    /**
     * @param repeatTimes The amount of times for the texture to repeat over the y-axis.
     */
    public void setTexRepeatX(float repeatTimes) {
        this.texRepeatX = repeatTimes;
        this.model = this.toTexturedModel();
    }

    public float getTexRepeatY() {
        return texRepeatY;
    }

    public float getTexRepeatX() {
        return texRepeatX;
    }

    protected float[] getTexCoords() {
        return new float[]{
                0, 0,
                this.texRepeatY, 0,
                this.texRepeatY, this.texRepeatX,
                0, this.texRepeatX,
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
    protected TexturedModel toTexturedModel() {
        return new TexturedModel(this.calculateVertices(), this.getIndices(), this.getTexCoords());
    }

    /**
     * The close model needs to be closed at the end of a Rect's usage to prevent a memory leak
     */
    @Override
    public void close() {
        this.model.close();
    }
}
