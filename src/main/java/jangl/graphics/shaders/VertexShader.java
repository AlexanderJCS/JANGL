package jangl.graphics.shaders;

import java.io.InputStream;
import java.io.UncheckedIOException;

public class VertexShader extends Shader {

    public VertexShader(String filepath, ShaderType shaderType) throws UncheckedIOException {
        super(filepath, shaderType);
    }

    public VertexShader(InputStream shaderStream, ShaderType shaderType) throws UncheckedIOException {
        super(shaderStream, shaderType);
    }
}
