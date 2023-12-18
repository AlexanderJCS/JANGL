package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.models.TexturedModel;

/**
 * The "base" of every other rectangular object. Used for collision and conversion to a Model or TexturedModel class.
 */
public class Rect extends Shape {
    private final float x1, y1, x2, y2;
    private final float width, height;

    /**
     * @param topLeft The top left of the rect
     * @param width   The width of the rect, units of world coords
     * @param height  The height of the rect, units of world coords
     */
    public Rect(WorldCoords topLeft, float width, float height) {
        this.width = width;
        this.height = height;

        this.x1 = -this.width / 2;
        this.y1 = this.height / 2;
        this.x2 = this.width / 2;
        this.y2 = -this.height / 2;

        this.model = this.toTexturedModel();

        WorldCoords realTopLeft = new WorldCoords(topLeft.x, topLeft.y);
        realTopLeft.x += this.x2;
        realTopLeft.y += this.y2;

        this.transform.shift(realTopLeft);
    }

    /**
     * @return The width of the rectangle, not taking into account the scaling of the transform.
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return The height of the rectangle, not taking into account the scaling of the transform.
     */
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
    public int[] getIndices() {
        return new int[]{
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
        super.bindShader();

        if (super.shouldDraw()) {
            super.draw();
            this.model.render();
        }
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
