package jangl.graphics.shaders;

import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Use this class to create a VertexShader.
 */
public class VertexShader extends Shader {
    public VertexShader(String filepath) throws UncheckedIOException {
        super(filepath);
    }

    public VertexShader(InputStream shaderStream) throws UncheckedIOException {
        super(shaderStream);
    }
}
