package jangl.graphics.models;

import static org.lwjgl.opengl.GL46.*;

/**
 * Allows you to pass the vertex and index information of a shape into OpenGL. Passing indices reduces video memory,
 * so it is recommended compared to the base Model class.
 */
public class IndicesModel extends Model {
    protected int iID;

    public IndicesModel(float[] vertices, int[] indices) {
        super(vertices);

        this.drawCount = indices.length;
        this.iID = glGenBuffers();
        this.setIndices(indices);
    }

    /**
     * Renders the model as a white box. Use a ColorShaderProgram to chance the color, or a TexturedModel to apply a texture.
     */
    @Override
    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, this.vID);
        glVertexPointer(DIMENSIONS, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, this.drawCount);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iID);
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
    }

    private void setIndices(int[] indices) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void subIndices(int[] indices, int offset) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iID);
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, offset, indices);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void close() {
        super.close();
        glDeleteBuffers(iID);
    }
}
