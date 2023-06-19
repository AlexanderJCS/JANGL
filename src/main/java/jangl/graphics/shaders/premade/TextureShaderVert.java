package jangl.graphics.shaders.premade;

import jangl.graphics.shaders.Shader;
import jangl.graphics.shaders.VertexShader;

import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL46.glUniform1i;
import static org.lwjgl.opengl.GL46.glGetUniformLocation;

public class TextureShaderVert extends VertexShader {
    private final boolean obeyCamera;

    /**
     * @param obeyCamera True to have the object move with the camera. False to keep the object stationary on the screen
     *                   when the camera moves.
     * @throws UncheckedIOException Throws an UncheckedIOException if it cannot find the texture shader. Normally, this
     *                              should not happen.
     */
    public TextureShaderVert(boolean obeyCamera) throws UncheckedIOException {
        super(Shader.class.getResourceAsStream("/shaders/textureShader/textureShader.vert"));
        this.obeyCamera = obeyCamera;
    }

    @Override
    public void setUniforms(int programID) {
        int uniformLocation = glGetUniformLocation(programID, "obeyCamera");
        glUniform1i(uniformLocation, this.obeyCamera ? 1 : 0);
    }
}
