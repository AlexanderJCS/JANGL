package jangl.graphics.models;

import jangl.util.BufferManager;

import static org.lwjgl.opengl.GL46.*;

/**
 * Modified from:
 * <a href="https://www.youtube.com/watch?v=-6P_CkT-FlQ&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=5&ab_channel=WarmfulDevelopment">...</a>
 * Model class, which allows a shape to be drawn to the screen.
 * <p>
 * Also see inheritors of this class: TriangleFanModel, IndicesModel, and TexturedModel, which inherits IndicesModel.
 */
public class Model implements AutoCloseable {
    protected static final int DIMENSIONS = 2;
    protected int drawCount;
    protected int vId;

    /**
     * Create a new model with the given vertices.
     *
     * @param vertices The triangle vertices.
     */
    public Model(float[] vertices) {
        this.drawCount = vertices.length / DIMENSIONS;

        BufferManager.setFloatBuffer(BufferManager.VBO_BUFFER, vertices);

        this.vId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.vId);
        glBufferData(GL_ARRAY_BUFFER, BufferManager.VBO_BUFFER, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Renders the model as a white box.
     */
    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, this.vId);
        glVertexPointer(DIMENSIONS, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, this.drawCount);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
    }

    /**
     * Changes the vertices to the ones given.
     */
    public void changeVertices(float[] vertices) {
        // http://forum.lwjgl.org/index.php?topic=5334.0

        glBindBuffer(GL_ARRAY_BUFFER, this.vId);
        BufferManager.setFloatBuffer(BufferManager.VBO_BUFFER, vertices);
        glBufferSubData(GL_ARRAY_BUFFER, 0, BufferManager.VBO_BUFFER);
        glBindBuffer(GL_ARRAY_BUFFER, this.vId);
    }

    /**
     * Close method needs to be called at the end of a Model's lifespan to prevent memory leaks.
     */
    @Override
    public void close() {
        glDeleteBuffers(vId);
    }
}
