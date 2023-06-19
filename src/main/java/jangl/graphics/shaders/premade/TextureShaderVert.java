package jangl.graphics.shaders.premade;

import jangl.graphics.shaders.Shader;
import jangl.graphics.shaders.ShaderType;

import java.io.UncheckedIOException;

public class TextureShaderVert extends Shader {
    public TextureShaderVert() throws UncheckedIOException {
        super(ColorShader.class.getResourceAsStream("/shaders/textureShader/textureShader.vert"), ShaderType.VERTEX);
    }
}
