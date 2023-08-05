package jangl.graphics.models;

import static org.lwjgl.opengl.GL41.*;

/**
 * Modified from:
 * <a href="https://www.youtube.com/watch?v=-6P_CkT-FlQ&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=5&ab_channel=WarmfulDevelopment">...</a>
 * Model class, which allows a shape to be drawn to the screen.
 * <p>
 * Also see inheritors of this class: TriangleFanModel, IndicesModel, and TexturedModel, which inherits IndicesModel.
 */
public class Model implements AutoCloseable {
    protected static final int DIMENSIONS = 2;
    protected static int drawCallCounter = 0;
    protected int drawCount;
    protected int vID;

    /**
     * Create a new model with the given vertices.
     *
     * @param vertices The triangle vertices.
     */
    public Model(float[] vertices) {
        this.drawCount = vertices.length / DIMENSIONS;

        this.vID = glGenBuffers();
        this.setVertices(vertices);
    }

    /**
     * Renders the model as a white box.
     */
    public void render() {
        drawCallCounter++;
        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, this.vID);
        glVertexPointer(DIMENSIONS, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, this.drawCount);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
    }

    /**
     * Changes the vertices to the ones given.
     */
    private void setVertices(float[] vertices) {
        glBindBuffer(GL_ARRAY_BUFFER, this.vID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void subVertices(float[] vertices, int offset) {
        glBindBuffer(GL_ARRAY_BUFFER, this.vID);
        glBufferSubData(GL_ARRAY_BUFFER, offset, vertices);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static int getDrawCallCount() {
        return drawCallCounter;
    }

    public static void resetDrawCallCounter() {
        drawCallCounter = 0;
    }

    /**
     * Close method needs to be called at the end of a Model's lifespan to prevent memory leaks.
     */
    @Override
    public void close() {
        glDeleteBuffers(vID);
    }
}
