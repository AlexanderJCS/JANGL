package jangl.graphics.shaders;

import jangl.graphics.Camera;
import org.joml.Matrix4f;
import org.joml.Vector2i;

import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private String removeMultiLineComments(String source) {
        Matcher matcher = Pattern.compile("/\\*|\\*/").matcher(source);

        // A list of commented indices. Items with even indices in the list are beginnings of comments,
        // and odd indices are endings of comments
        List<Integer> commentedIndices = new ArrayList<>();

        while (matcher.find()) {
            if (commentedIndices.size() % 2 == 0 && matcher.group().equals("/*")) {
                commentedIndices.add(matcher.start());

            } else if (matcher.group().equals("*/")) {
                commentedIndices.add(matcher.end());
            }
        }

        if (commentedIndices.size() % 2 != 0) {
            commentedIndices.add(source.length());
        }

        if (commentedIndices.size() == 0) {
            return source;
        }

        StringBuilder sourceBuilder = new StringBuilder(source.substring(0, commentedIndices.get(0)));

        for (int i = 1; i < commentedIndices.size(); i += 2) {
            if (i + 1 < commentedIndices.size()) {
                sourceBuilder.append(source, commentedIndices.get(i), commentedIndices.get(i + 1));
            } else {
                sourceBuilder.append(source, commentedIndices.get(i), source.length());
            }
        }

        return sourceBuilder.toString();
    }

    @Override
    protected String precompile(String source) {
        source = this.removeMultiLineComments(source);

        // Now move on to line-by-line precompilation
        StringBuilder builder = new StringBuilder();

        boolean lineAfterVersion = false;

        for (String line : source.split("\n")) {
            line = line.replace("\r", "");

            if (lineAfterVersion) {
                builder.append(Camera.UBO_CODE);
                builder.append("uniform mat4 modelMatrix;uniform bool obeyCamera;\n");
                lineAfterVersion = false;
            }

            if (line.contains("#version")) {
                lineAfterVersion = true;
            }

            // Remove comments from the code to prevent } characters that are commented out affecting precompilation
            if (line.contains("//")) {
                line = line.substring(0, line.indexOf("//"));
            }

            if (line.contains("gl_Position") && line.contains("=")) {
                line = line.replace(" ", "");
                String lineWithoutGlPosition = line.replace("gl_Position=", "");

                line = "if (obeyCamera) {gl_Position=projectionMatrix*cameraMatrix*modelMatrix*" + lineWithoutGlPosition + "} else {gl_Position=projectionMatrix*modelMatrix*" + lineWithoutGlPosition + "}";
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
    public void setMatrixUniforms(int programID, Matrix4f modelMatrix) {
        this.addMatrixUniform(programID, "modelMatrix", modelMatrix);

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
