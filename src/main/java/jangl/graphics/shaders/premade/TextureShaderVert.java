package jangl.graphics.shaders.premade;

import jangl.graphics.shaders.AttribLocation;
import jangl.graphics.shaders.Shader;
import jangl.graphics.shaders.VertexShader;

import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;

/**
 * A shader to pass texture coords to a fragment shader.
 */
public class TextureShaderVert extends VertexShader {

    /**
     * @throws UncheckedIOException Throws an UncheckedIOException if it cannot find the texture shader. Normally, this
     *                              should not happen.
     */
    public TextureShaderVert() throws UncheckedIOException {
        super(Shader.class.getResourceAsStream("/shaders/textureShader/textureShader.vert"));
    }

    public static List<AttribLocation> getAttribLocations() {
        return Arrays.asList(
                new AttribLocation(0, "vertices"),
                new AttribLocation(1, "textures")
        );
    }
}
