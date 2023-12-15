package jangl.graphics.models;

import jangl.graphics.Bindable;
import jangl.resourcemanager.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.opengl.GL41.*;


public class Model implements AutoCloseable, Bindable {
    protected static final int DIMENSIONS = 2;
    protected static int drawCallCounter = 0;
    protected int drawCount;
    protected int vao;
    protected int vbo;
    protected AtomicBoolean closed;


    /**
     * Create a new model with the given vertices.
     *
     * @param vertices The triangle vertices.
     */
    public Model(float[] vertices) {
        this.drawCount = vertices.length / DIMENSIONS;

        this.vao = glGenVertexArrays();
        this.vbo = glGenBuffers();

        glBindVertexArray(this.vao);
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // stride = 0 means a tightly-packed array
        glVertexAttribPointer(0, DIMENSIONS, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);

        this.closed = new AtomicBoolean(false);

        ResourceManager.add(
                this,
                new ResourceQueuer(
                        this.closed,
                        new Resource(this.getBuffers(), ResourceType.BUFFER)
                )
        );

        ResourceManager.add(
                this,
                new ResourceQueuer(
                        this.closed,
                        new Resource(this.vao, ResourceType.VAO)
                )
        );
    }

    /**
     * @return An array of all the buffer IDs, used for memory management.
     */
    protected int[] getBuffers() {
        return new int[]{ this.vbo};
    }

    /**
     * Renders the model as a white box.
     */
    public void render() {
        this.bind();
        glDrawArrays(GL_TRIANGLES, 0, this.drawCount);
        this.unbind();
    }

    @Override
    public void bind() {
        glBindVertexArray(this.vao);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    public void subVertices(float[] vertices, int offset) {
        glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
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
     * Close method can to be called at the end of a Model's lifespan to prevent memory leaks.
     */
    @Override
    public void close() {
        if (this.closed.getAndSet(true)) {
            return;
        }

        glDeleteVertexArrays(this.vao);
        glDeleteBuffers(this.getBuffers());
    }
}
