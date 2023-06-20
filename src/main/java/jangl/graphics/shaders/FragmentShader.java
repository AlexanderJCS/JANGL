package jangl.graphics.shaders;

import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Use this to create a fragment shader.
 */
public class FragmentShader extends Shader {
    public FragmentShader(String filepath) throws UncheckedIOException {
        super(filepath);
    }

    public FragmentShader(InputStream shaderStream) throws UncheckedIOException {
        super(shaderStream);
    }
}
