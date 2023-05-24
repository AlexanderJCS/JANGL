package jangl.graphics.shaders;

public class ShaderCompileException extends RuntimeException {
    public ShaderCompileException() {

    }

    public ShaderCompileException(String message) {
        super(message);
    }

    public ShaderCompileException(Throwable cause) {
        super(cause);
    }

    public ShaderCompileException(String message, Throwable cause) {
        super(message, cause);
    }
}
