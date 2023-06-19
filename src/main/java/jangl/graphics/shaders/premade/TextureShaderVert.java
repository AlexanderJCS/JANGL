package jangl.graphics.shaders.premade;

import jangl.graphics.shaders.Shader;
import jangl.graphics.shaders.ShaderType;
import jangl.graphics.shaders.VertexShader;

import java.io.UncheckedIOException;

public class TextureShaderVert extends VertexShader {
    public TextureShaderVert() throws UncheckedIOException {
        super(Shader.class.getResourceAsStream("/shaders/textureShader/textureShader.vert"), ShaderType.VERTEX);
    }
}
