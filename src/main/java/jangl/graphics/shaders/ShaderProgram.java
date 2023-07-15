package jangl.graphics.shaders;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;

/**
 * A ShaderProgram allows you to combine a FragmentShader and VertexShader into a single program.
 */
public class ShaderProgram implements AutoCloseable {
    private final int programID;
    private final List<Integer> shaderIDs;
    private final List<Shader> shaders;
    private static ShaderProgram boundProgram;

    /**
     * WARNING: not including a fragment shader may result in the object being black and appearing to be invisible.
     * If you experience this, you can include a ColorShader fragment shader.
     *
     * @param vs The vertex shader to add to the program.
     */
    public ShaderProgram(VertexShader vs) {
        this(vs, null, new ArrayList<>());
    }

    /**
     * @param fs The fragment shader to add to the program.
     */
    public ShaderProgram(FragmentShader fs) {
        this(null, fs, new ArrayList<>());
    }

    /**
     * @param vs The vertex shader to add to the program.
     * @param attribLocations A list of attribute locations for the shader.
     */
    public ShaderProgram(VertexShader vs, List<AttribLocation> attribLocations) {
        this(vs, null, attribLocations);
    }

    /**
     * @param fs The fragment shader to add to the program.
     * @param attribLocations A list of attribute locations for the shader.
     */
    public ShaderProgram(FragmentShader fs, List<AttribLocation> attribLocations) {
        this(null, fs, attribLocations);
    }

    /**
     * @param vs The vertex shader to add to the program.
     * @param fs The fragment shader to add to the program.
     */
    public ShaderProgram(VertexShader vs, FragmentShader fs) {
        this(vs, fs, new ArrayList<>());
    }

    /**
     * @param vs The vertex shader to add to the program.
     * @param fs The fragment shader to add to the program.
     * @param attribLocations A list of attribute locations for the shader.
     * @throws ShaderCompileException Throws if the shaders cannot compile, link, or validate.
     */
    public ShaderProgram(VertexShader vs, FragmentShader fs, List<AttribLocation> attribLocations) throws ShaderCompileException {
        this.shaderIDs = new ArrayList<>();
        this.shaders = new ArrayList<>();

        if (vs != null) {
            this.shaders.add(vs);
        } if (fs != null) {
            this.shaders.add(fs);
        }

        // Compile the shaders
        for (Shader shader : this.shaders) {
            int shaderType = shader instanceof VertexShader ? GL_VERTEX_SHADER : GL_FRAGMENT_SHADER;
            this.shaderIDs.add(compileShader(shader.sourceCode, shaderType));
        }

        this.programID = glCreateProgram();

        // Attach the shader to the program
        for (int shaderID : this.shaderIDs) {
            glAttachShader(this.programID, shaderID);
        }

        // Bind attribute locations
        for (AttribLocation attribLocation : attribLocations) {
            glBindAttribLocation(this.programID, attribLocation.index(), attribLocation.name());
        }

        // Link and validate the programs. Check for errors.
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
     * @param type The type of shader program (either GL_VERTEX_SHADER or GL_FRAGMENT_SHADER)
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
        boundProgram = null;
    }

    /**
     * Bind the sander. Run this when you want the shader to apply to objects that you draw.
     */
    public void bind() {
        glUseProgram(programID);
        boundProgram = this;

        for (Shader shader : this.shaders) {
            shader.setUniforms(this.programID);
        }
    }

    public static ShaderProgram getBoundProgram() {
        return boundProgram;
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

    /**
     * @return The shader program's vertex shader if one exists. Otherwise, it returns null.
     */
    public VertexShader getVertexShader() {
        for (Shader shader : this.shaders) {
            if (VertexShader.class.isAssignableFrom(shader.getClass())) {
                return (VertexShader) shader;
            }
        }

        return null;
    }

    /**
     * @return The shader program's fragment shader if one exists. Otherwise, it returns null.
     */
    public FragmentShader getFragmentShader() {
        for (Shader shader : this.shaders) {
            if (FragmentShader.class.isAssignableFrom(shader.getClass())) {
                return (FragmentShader) shader;
            }
        }

        return null;
    }

    public int getProgramID() {
        return programID;
    }
}
