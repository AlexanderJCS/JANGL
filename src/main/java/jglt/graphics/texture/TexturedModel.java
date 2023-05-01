package jglt.graphics.texture;

import jglt.graphics.BufferManager;
import jglt.graphics.Model;

import static org.lwjgl.opengl.GL21.*;


/**
 * A Model class that allows textures to be drawn to it.
 * <p>
 * Modified from this tutorial:
 * <a href="https://www.youtube.com/watch?v=-6P_CkT-FlQ&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=5&ab_channel=WarmfulDevelopment">...</a>
 */
public class TexturedModel extends Model {
    private final int tId;

    /**
     * @param vertices  The vertices.
     * @param texCoords Which corner of the texture should be mapped to what corner of the model.
     */
    public TexturedModel(float[] vertices, float[] texCoords) {
        super(vertices);

        tId = glGenBuffers();
        BufferManager.setFloatBuffer(BufferManager.texCoordsBuffer, texCoords);
        glBindBuffer(GL_ARRAY_BUFFER, tId);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * Bind the texture then run this method. The texture will be placed on top of this model.
     */
    @Override
    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        // Bind the vertices buffer
        glBindBuffer(GL_ARRAY_BUFFER, vId);
        glVertexPointer(DIMENSIONS, GL_FLOAT, 0, 0);

        // Bind the texture buffer
        glBindBuffer(GL_ARRAY_BUFFER, tId);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        // Draw the vertices with the texture
        glDrawArrays(GL_TRIANGLES, 0, drawCount);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    @Override
    public void close() {
        super.close();
        glDeleteBuffers(tId);
    }
}
