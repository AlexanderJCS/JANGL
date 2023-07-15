package jangl.graphics.shaders;

import org.joml.Matrix4f;

import java.io.InputStream;
import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL20.*;

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

    @Override
    protected String precompile(String source) {
        StringBuilder builder = new StringBuilder();

        boolean lineAfterVersion = false;
        for (String line : source.split("\n")) {
            // No idea but for some reason it likes to make the end of the line have the escape sequence \r
            line = line.replace("\r", "\n");

            if (lineAfterVersion) {
                builder.append("uniform mat4 projectionMatrix;\n");
                lineAfterVersion = false;
            }

            if (line.contains("#version")) {
                lineAfterVersion = true;
            }

            if (line.contains("gl_Position") && line.contains("=")) {
                line = line.replace(" ", "");
                line = line.replace("gl_Position=", "gl_Position=projectionMatrix*");
            }

            builder.append(line).append("\n");
        }

        System.out.println(builder);

        return super.precompile(builder.toString());
    }

    /**
     * Sets the projection uniform of the shader program. If there is no projection matrix uniform, the method will
     * return without doing anything.
     *
     * @param programID The program ID to the pass the uniform to
     */
    public void setProjectionUniform(int programID, Matrix4f projectionMatrix) {
        int projectionUniformLocation = glGetUniformLocation(programID, "projectionMatrix");

        // If there's no projection matrix uniform, leave silently
        if (projectionUniformLocation == -1) {
            return;
        }

        float[] projectionMatrixArr = new float[16];
        projectionMatrix.get(projectionMatrixArr);

        glUniformMatrix4fv(projectionUniformLocation, false, projectionMatrixArr);
    }
}
