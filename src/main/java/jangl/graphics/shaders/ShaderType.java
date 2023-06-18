package jangl.graphics.shaders;

import static org.lwjgl.opengl.GL46.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL46.GL_VERTEX_SHADER;

public enum ShaderType {
    FRAGMENT, VERTEX;

    /**
     * Converts a ShaderType to an OpenGL shader type value.
     * @return The OpenGL shader type value.
     */
    public static int toOpenGLType(ShaderType type) {
        return type == FRAGMENT ? GL_FRAGMENT_SHADER : GL_VERTEX_SHADER;
    }
}
