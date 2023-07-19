package jangl.graphics.shaders.exceptions;

public class ShaderPrecompileException extends ShaderCompileException {
    public ShaderPrecompileException() {

    }

    public ShaderPrecompileException(String message) {
        super(message);
    }

    public ShaderPrecompileException(Throwable cause) {
        super(cause);
    }

    public ShaderPrecompileException(String message, Throwable cause) {
        super(message, cause);
    }
}
