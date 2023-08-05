package jangl.graphics.shaders.premade;

import jangl.graphics.shaders.FragmentShader;
import jangl.graphics.shaders.Shader;

import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL41.glGetUniformLocation;
import static org.lwjgl.opengl.GL46.glUniform1i;

public class TextureShaderFrag extends FragmentShader {
    public TextureShaderFrag() throws UncheckedIOException {
        super(Shader.class.getResourceAsStream("/shaders/textureShader/textureShader.frag"));
    }

    @Override
    public void setUniforms(int programID) {
        int location = glGetUniformLocation(programID, "texSampler");
        glUniform1i(location, 0);
    }
}
