package jangl.graphics.shaders.premade;

import static org.lwjgl.opengl.GL46.*;

import jangl.graphics.shaders.FragmentShader;
import jangl.graphics.shaders.Shader;
import jangl.graphics.shaders.ShaderType;

import java.io.UncheckedIOException;

public class TextureShaderFrag extends FragmentShader {
    public TextureShaderFrag() throws UncheckedIOException {
        super(Shader.class.getResourceAsStream("/shaders/textureShader/textureShader.frag"), ShaderType.FRAGMENT);
    }

    @Override
    public void setUniforms(int programID) {
        int location = glGetUniformLocation(programID, "texSampler");
        glUniform1i(location, 0);
    }
}
