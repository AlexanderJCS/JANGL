package jangl.graphics.shaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;

public class ShaderProgram implements AutoCloseable {
    private final int programID;
    private final List<Integer> shaderIDs;
    private final List<Shader> shaders;

    public ShaderProgram(Shader shader) {
        this(Collections.singletonList(shader));
    }

    public ShaderProgram(List<Shader> shaders) throws ShaderCompileException {
        this.shaderIDs = new ArrayList<>();
        this.shaders = shaders;

        for (Shader shader : shaders) {
            this.shaderIDs.add(compileShader(shader.sourceCode, shader.type));
        }

        this.programID = glCreateProgram();

        for (int shaderID : this.shaderIDs) {
            glAttachShader(this.programID, shaderID);
        }

        glLinkProgram(this.programID);

        if (glGetProgrami(this.programID, GL_LINK_STATUS) == GL_FALSE) {
            throw new ShaderCompileException(
                    "Could not link shader program.\nError message:\n" +
                            glGetShaderInfoLog(this.programID, 8192)
            );
        }

        glValidateProgram(this.programID);

        if (glGetProgrami(this.programID, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new ShaderCompileException(
                    "Could not validate shader program.\nError message:\n" +
                            glGetShaderInfoLog(this.programID, 8192)
            );
        }
    }

    /**
     * @param program The shader program source code
     * @param type    The type of shader program (either GL_VERTEX_SHADER or GL_FRAGMENT_SHADER)
     * @return The ID of the compiled shader
     */
    private static int compileShader(String program, int type) throws ShaderCompileException {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, program);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            String shaderTypeString = type == GL_VERTEX_SHADER ? "vertex" : "fragment";

            throw new ShaderCompileException(
                    "Could not compile " + shaderTypeString + " shader.\nError message:\n" +
                            glGetShaderInfoLog(shaderID, 8192)
            );
        }

        return shaderID;
    }

    /**
     * Unbind the shader. Run this when you do not want the shader to apply to any more objects that you draw.
     */
    public static void unbind() {
        glUseProgram(0);
    }

    /**
     * Bind the sander. Run this when you want the shader to apply to objects that you draw.
     */
    public void bind() {
        glUseProgram(programID);

        for (Shader shader : this.shaders) {
            shader.setUniforms(this.programID);
        }
    }

    /**
     * Delete all information relating to the shader to avoid memory leaks.
     */
    @Override
    public void close() {
        ShaderProgram.unbind();

        for (int shaderID : this.shaderIDs) {
            glDetachShader(this.programID, shaderID);
            glDeleteShader(shaderID);
        }

        glDeleteProgram(this.programID);
    }
}
