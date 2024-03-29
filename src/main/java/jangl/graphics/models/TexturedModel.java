package jangl.graphics.models;

import static org.lwjgl.opengl.GL41.*;


/**
 * A Model class that allows textures to be drawn to it.
 */
public class TexturedModel extends IndicesModel {
    protected final int tbo;

    /**
     * @param vertices  The vertices.
     * @param texCoords Which corner of the texture should be mapped to what corner of the model.
     */
    public TexturedModel(float[] vertices, int[] indices, float[] texCoords) {
        super(vertices, indices);

        glBindVertexArray(this.vao);

        this.tbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.tbo);
        glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);

        // Set up the attribute pointer for the texture coordinates
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
    }

    @Override
    protected int[] getBuffers() {
        return new int[]{ this.vbo, this.ebo, this.tbo};
    }

    public void subTexCoords(float[] texCoords, int offset) {
        glBindBuffer(GL_TEXTURE_BUFFER, this.tbo);
        glBufferSubData(GL_TEXTURE_BUFFER, offset, texCoords);
        glBindBuffer(GL_TEXTURE_BUFFER, 0);
    }
}
