package jangl.graphics.models;

import static org.lwjgl.opengl.GL46.*;


/**
 * A Model class that allows textures to be drawn to it.
 */
public class TexturedModel extends IndicesModel {
    // Modified (heavily) from this tutorial:
    // https://www.youtube.com/watch?v=-6P_CkT-FlQ&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=5&ab_channel=WarmfulDevelopment

    private final int tID;

    /**
     * @param vertices  The vertices.
     * @param texCoords Which corner of the texture should be mapped to what corner of the model.
     */
    public TexturedModel(float[] vertices, int[] indices, float[] texCoords) {
        super(vertices, indices);

        this.tID = glGenBuffers();
        this.setTexCoords(texCoords);
    }

    /**
     * Bind the texture then run this method. The texture will be placed on top of this model.
     */
    @Override
    public void render() {
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);

        glBindBuffer(GL_ARRAY_BUFFER, this.vID);
        glVertexPointer(DIMENSIONS, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, this.tID);
        glTexCoordPointer(2, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iID);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisable(GL_BLEND);

        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
    }

    private void setTexCoords(float[] texCoords) {
        glBindBuffer(GL_ARRAY_BUFFER, this.tID);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void subTexCoords(float[] texCoords, int offset) {
        glBindBuffer(GL_ARRAY_BUFFER, this.tID);
        glBufferSubData(GL_ARRAY_BUFFER, offset, texCoords);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void close() {
        super.close();
        glDeleteBuffers(tID);
    }
}
