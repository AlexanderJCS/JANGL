package jangl.graphics.models;

import static org.lwjgl.opengl.GL41.*;

/**
 * Allows you to pass the vertex and index information of a shape into OpenGL. Passing indices reduces video memory,
 * so it is recommended compared to the base Model class.
 */
public class IndicesModel extends Model {
    protected int EBO;

    public IndicesModel(float[] vertices, int[] indices) {
        super(vertices);

        glBindVertexArray(this.VAO);
        this.drawCount = indices.length;
        this.EBO = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    @Override
    public void render() {
        this.bind();
        glDrawElements(GL_TRIANGLES, this.drawCount, GL_UNSIGNED_INT, 0);
        this.unbind();
    }

    public void subIndices(int[] indices, int offset) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void close() {
        glDeleteBuffers(this.EBO);
        super.close();
    }
}
