package jangl.graphics.models;

import static org.lwjgl.opengl.GL41.*;


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
        drawCallCounter++;
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, this.vID);
        glVertexAttribPointer(0, DIMENSIONS, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, this.tID);
        glVertexAttribPointer(1, DIMENSIONS, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.iID);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDrawElements(GL_TRIANGLES, drawCount, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisable(GL_BLEND);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
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
