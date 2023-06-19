package jangl.graphics.shaders;

import java.io.InputStream;
import java.io.UncheckedIOException;

public class FragmentShader extends Shader {
    public FragmentShader(String filepath, ShaderType shaderType) throws UncheckedIOException {
        super(filepath, shaderType);
    }

    public FragmentShader(InputStream shaderStream, ShaderType shaderType) throws UncheckedIOException {
        super(shaderStream, shaderType);
    }
}
