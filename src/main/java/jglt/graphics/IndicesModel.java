package jglt.graphics;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public class IndicesModel extends Model {
    protected int iId;

    public IndicesModel(float[] vertices, int[] indices) {
        super(vertices);

        BufferManager.setIntBuffer(BufferManager.indicesBuffer, indices);
        this.drawCount = indices.length;
        this.iId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferManager.indicesBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Renders the model as a white box.
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
