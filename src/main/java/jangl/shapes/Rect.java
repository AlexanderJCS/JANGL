package jangl.shapes;

import jangl.coords.NDCoords;
import jangl.graphics.models.TexturedModel;

/**
 * The "base" of every other rectangular object. Used for collision and conversion to a Model or TexturedModel class.
 */
public class Rect extends Shape {
    private float texRepeatY, texRepeatX;
    private final float x1, y1, x2, y2;
    private final float width, height;

    /**
     * @param center the center of the rect
     * @param width   The width of the rect, units of normalized device coordinates
     * @param height  The height of the rect, units of normalized device coordinates
     */
    public Rect(NDCoords center, float width, float height) {
        this.x1 = -width / 2;
        this.y1 = height / 2;
        this.x2 = width / 2;
        this.y2 = -height / 2;

        this.texRepeatY = 1;
        this.texRepeatX = 1;

        this.width = width;
        this.height = height;

        this.model = this.toTexturedModel();
        this.transform.setCenter(center.toVector2f());
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public float[] calculateVertices() {
        return new float[]{
            x1, y1,  // Top left
            x2, y1,  // Top right
            x2, y2,  // Bottom right
            x1, y2,  // Bottom left
        };
    }

    @Override
    public float[] getExteriorVertices() {
        return this.calculateVerticesMatrix();
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
        super.draw();
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
