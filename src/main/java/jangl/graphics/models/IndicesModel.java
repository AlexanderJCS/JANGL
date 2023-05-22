package jangl.graphics.models;

import jangl.util.BufferManager;

import static org.lwjgl.opengl.GL46.*;

/**
 * Allows you to pass the vertex and index information of a shape into OpenGL. Passing indices reduces video memory,
 * so it is recommended compared to the base Model class.
 */
public class IndicesModel extends Model {
    protected int iId;

    public IndicesModel(float[] vertices, int[] indices) {
        super(vertices);

        BufferManager.setIntBuffer(BufferManager.INDICES_BUFFER, indices);
        this.drawCount = indices.length;
        this.iId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferManager.INDICES_BUFFER, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Renders the model as a white box. Use a ColorShader to chance the color, or a TexturedModel to apply a texture.
     */
    @Override
    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, this.vId);
        glVertexPointer(DIMENSIONS, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, this.drawCount);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iId);
        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
    }
}
