package jangl.graphics.models;

import jangl.graphics.Bindable;

import static org.lwjgl.opengl.GL41.*;


public class Model implements AutoCloseable, Bindable {
    protected static final int DIMENSIONS = 2;
    protected static int drawCallCounter = 0;
    protected int drawCount;
    protected int VAO;
    protected int VBO;

    /**
     * Create a new model with the given vertices.
     *
     * @param vertices The triangle vertices.
     */
    public Model(float[] vertices) {
        this.drawCount = vertices.length / DIMENSIONS;

        this.VAO = glGenVertexArrays();
        this.VBO = glGenBuffers();

        glBindVertexArray(this.VAO);
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // stride = 0 means a tightly-packed array
        glVertexAttribPointer(0, DIMENSIONS, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);
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
        glBindVertexArray(this.VAO);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    /**
     * Changes the vertices to the ones given.
     */
    private void setVertices(float[] vertices) {
        // TODO: check if this works
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void subVertices(float[] vertices, int offset) {
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
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
        glDeleteVertexArrays(this.VAO);
        glDeleteBuffers(this.VBO);
    }
}
