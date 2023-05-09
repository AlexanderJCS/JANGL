package jglt.graphics.models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

/**
 * Takes in the vertices for a triangle fan to draw. Used for the Circle class.
 */
public class TriangleFanModel extends Model {
    public TriangleFanModel(float[] vertices) {
        super(vertices);
    }

    @Override
    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, this.vId);
        glVertexPointer(DIMENSIONS, GL_FLOAT, 0, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, this.drawCount);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_VERTEX_ARRAY);
    }
}
