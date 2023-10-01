package jangl.graphics.models;

import static org.lwjgl.opengl.GL41.*;


/**
 * A Model class that allows textures to be drawn to it.
 */
public class TexturedModel extends IndicesModel {
    private final int TBO;

    /**
     * @param vertices  The vertices.
     * @param texCoords Which corner of the texture should be mapped to what corner of the model.
     */
    public TexturedModel(float[] vertices, int[] indices, float[] texCoords) {
        super(vertices, indices);

        glBindVertexArray(this.VAO);

        this.TBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.TBO);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);

        // Set up the attribute pointer for the texture coordinates
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }


    public void subTexCoords(float[] texCoords, int offset) {
        glBindBuffer(GL_TEXTURE_BUFFER, this.TBO);
        glBufferSubData(GL_TEXTURE_BUFFER, offset, texCoords);
        glBindBuffer(GL_TEXTURE_BUFFER, 0);
    }

    @Override
    public void close() {
        super.close();
        glDeleteBuffers(TBO);
    }
}
