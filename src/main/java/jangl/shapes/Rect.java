package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.models.TexturedModel;

/**
 * The "base" of every other rectangular object. Used for collision and conversion to a Model or TexturedModel class.
 */
public class Rect extends Shape {
    private float x1, y1, x2, y2;
    private float width, height;
    private float texRepeatY, texRepeatX;

    /**
     * @param topLeft The top left of the rect
     * @param width   The width of the rect, units of world coords
     * @param height  The height of the rect, units of world coords
     */
    public Rect(WorldCoords topLeft, float width, float height) {
        this.texRepeatX = 1;
        this.texRepeatY = 1;

        this.width = width;
        this.height = height;

        this.refreshCorners();

        this.model = this.toTexturedModel();

        WorldCoords realTopLeft = new WorldCoords(topLeft.x, topLeft.y);
        realTopLeft.x += this.x2;
        realTopLeft.y += this.y2;

        this.transform.shift(realTopLeft);

        // Check the comments in setTexRepeatY to see why I'm re-doing this
        this.setTexRepeatX(1);
        this.setTexRepeatY(1);
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

    public float getTexRepeatY() {
        return this.texRepeatY;
    }

    /**
     * @param repeatTimes The amount of times for the texture to repeat over the x-axis.
     */
    public void setTexRepeatY(float repeatTimes) {
        // Subtract by 0.0000001 because sometimes the top pixel of the image transfers to the
        // bottom pixel. That bug is really weird and I don't know exactly what causes it (my guess is that the tex
        // coords are actually set to 1.000000015 or something because of floating point math) but it's really
        // frustrating and this is the only fix I found.
        // Same goes with setTexRepeatX().

        this.texRepeatY = repeatTimes - 0.0000001f;
        TexturedModel texturedModel = (TexturedModel) this.model;
        texturedModel.subTexCoords(this.getTexCoords(), 0);
    }

    public float getTexRepeatX() {
        return this.texRepeatX;
    }

    /**
     * @param repeatTimes The amount of times for the texture to repeat over the y-axis.
     */
    public void setTexRepeatX(float repeatTimes) {
        // If you're wondering why I subtract by 0.0000001f, read the comments in setTexRepeatY().

        this.texRepeatX = repeatTimes - 0.0000001f;
        TexturedModel texturedModel = (TexturedModel) this.model;
        texturedModel.subTexCoords(this.getTexCoords(), 0);
    }

    protected float[] getTexCoords() {
        return new float[]{
                0, 0,
                this.texRepeatX, 0,
                this.texRepeatX, this.texRepeatY,
                0, this.texRepeatY,
        };
    }


    /**
     * Sets the width of the rectangle.
     * <br>
     * WARNING: This method may be slow, since it needs to resend all the vertices from the CPU to GPU. It is
     *          highly recommended not to call this method many times per frame.
     *
     * @param newWidth The new width.
     */
    public void setWidth(float newWidth) {
        this.width = newWidth;
        this.refreshCorners();
        this.model.subVertices(calculateVertices(), 0);
    }

    /**
     * Sets the height of the rectangle.
     * <br>
     * WARNING: This method may be slow, since it needs to resend all the vertices from the CPU to GPU. It is
     *          highly recommended not to call this method many times per frame.
     *
     * @param newHeight The new height.
     */
    public void setHeight(float newHeight) {
        this.height = newHeight;
        this.refreshCorners();
        this.calculateVertices();
    }

    private void refreshCorners() {
        this.x1 = -width / 2;
        this.y1 = height / 2;
        this.x2 = width / 2;
        this.y2 = -height / 2;
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
