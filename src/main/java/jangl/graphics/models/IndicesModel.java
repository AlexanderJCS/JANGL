package jangl.graphics.models;

import static org.lwjgl.opengl.GL41.*;

/**
 * Allows you to pass the vertex and index information of a shape into OpenGL. Passing indices reduces video memory,
 * so it is recommended compared to the base Model class.
 */
public class IndicesModel extends Model {
    protected int ebo;

    public IndicesModel(float[] vertices, int[] indices) {
        super(vertices);

        glBindVertexArray(this.vao);
        this.drawCount = indices.length;
        this.ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    @Override
    protected int[] getBuffers() {
        return new int[]{ this.vbo, this.ebo};
    }

    @Override
    public void render() {
        this.bind();
        glDrawElements(GL_TRIANGLES, this.drawCount, GL_UNSIGNED_INT, 0);
        this.unbind();
    }

    public void subIndices(int[] indices, int offset) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
