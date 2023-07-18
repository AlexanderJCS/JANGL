package jangl.graphics.shaders;

import jangl.graphics.Camera;
import org.joml.Matrix4f;

import java.io.InputStream;
import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL20.*;

/**
 * Use this class to create a VertexShader.
 */
public class VertexShader extends Shader {
    private boolean obeyCamera;

    public VertexShader(String filepath) throws UncheckedIOException {
        super(filepath);
        this.obeyCamera = true;
    }

    public VertexShader(InputStream shaderStream) throws UncheckedIOException {
        super(shaderStream);
        this.obeyCamera = true;
    }

    @Override
    protected String precompile(String source) {
        StringBuilder builder = new StringBuilder();

        boolean lineAfterVersion = false;
        for (String line : source.split("\n")) {
            // No idea but for some reason it likes to make the end of the line have the escape sequence \r
            line = line.replace("\r", "\n");

            if (lineAfterVersion) {
                builder.append(Camera.UBO_CODE).append("\n");
                builder.append("uniform mat4 transformMatrix;\nuniform mat4 rotationMatrix;\nuniform bool obeyCamera;\n");
                lineAfterVersion = false;
            }

            if (line.contains("#version")) {
                lineAfterVersion = true;
            }

            if (line.contains("gl_Position") && line.contains("=")) {
                line = line.replace(" ", "");
                String lineWithoutGlPosition = line.replace("gl_Position=", "");

                line = "if (obeyCamera) {gl_Position=projectionMatrix*cameraMatrix*transformMatrix*rotationMatrix*" + lineWithoutGlPosition + "} else {gl_Position=projectionMatrix*transformMatrix*rotationMatrix*" + lineWithoutGlPosition + "}";
            }

            builder.append(line).append("\n");
        }

        return super.precompile(builder.toString());
    }

    private void addMatrixUniform(int programID, String matrixName, Matrix4f matrix) {
        int uniformLocation = glGetUniformLocation(programID, matrixName);

        // If there's no projection matrix uniform, leave silently
        if (uniformLocation == -1) {
            return;
        }

        float[] matrixArr = new float[16];
        matrix.get(matrixArr);

        glUniformMatrix4fv(uniformLocation, false, matrixArr);
    }

    /**
     * Sets the projection uniform of the shader program. If there is no projection matrix uniform, the method will
     * return without doing anything.
     *
     * @param programID The program ID to the pass the uniform to
     */
    public void setMatrixUniforms(int programID, Matrix4f transformMatrix, Matrix4f rotationMatrix) {
        this.addMatrixUniform(programID, "transformMatrix", transformMatrix);
        this.addMatrixUniform(programID, "rotationMatrix", rotationMatrix);

        int obeyCameraUniformLocation = glGetUniformLocation(programID, "obeyCamera");
        glUniform1i(obeyCameraUniformLocation, this.obeyCamera ? 1 : 0);
    }

    public boolean isObeyingCamera() {
        return this.obeyCamera;
    }

    public void setObeyCamera(boolean obeyCamera) {
        this.obeyCamera = obeyCamera;
    }
}
