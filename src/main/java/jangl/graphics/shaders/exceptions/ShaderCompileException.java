package jangl.graphics.shaders.exceptions;

public class ShaderCompileException extends RuntimeException {
    public ShaderCompileException() {

    }

    public ShaderCompileException(String shaderType, String compileError, String sourceCode) {
        super("Could not compile " + shaderType + " shader.\nError message:\n" + compileError + "\nSource code:\n" + sourceCode);
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
