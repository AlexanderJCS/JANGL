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

    /**
     * This method removes multi-line comments from the code. This is an important step of precompilation since
     * precompiling a vertex shader requires that the last closed curly brace is the end of the main function (this is
     * required since it will add more code before this curly brace).
     * <br>
     * Multi-line comments may mess up that step of precompilation if there is a multi-line comment after the main
     * function that has a closed curly brace. The code will incorrectly be placed in the comment instead of the end of
     * the main function. This method provides a solution.
     *
     * @param source The source code
     * @return The source code without any multi-line comments
     */
    private String removeMultiLineComments(String source) {
        /*
         * This algorithm works by following the steps below:
         * 1. Create a Matcher object that searches for open-multi-line (/*) or close-multi-line sequences of characters
         *
         * 2. Create a list of integers and iterate through each sequence of characters.
         *     2a. Even indices of the list will contain the index of the beginning of multi-line comments in the shader source code string.
         *     2b. Odd indices of the list will contain the index of the end of the multi-line comments in the shader source code string.
         *
         * 3. Extract comments from the source code string given the index of the comments found in step 2
         */

        // --- STEP 1: Create a matcher that searches for /* or */ character sequences ---
        Matcher matcher = Pattern.compile("/\\*|\\*/").matcher(source);


        // --- STEP 2: Find the beginning and end indices of the comments ---
        // A list of commented indices. Items with even indices in the list are beginnings of comments,
        // and odd indices are endings of comments
        List<Integer> commentedIndices = new ArrayList<>();

        while (matcher.find()) {
            // The if conditions are important here since you can have a multi-line comment which contains "/*" inside
            //  the comment itself, e.g.:
            /* This multi-line comment is an example of the edge case when I include "/*" */

            if (commentedIndices.size() % 2 == 0 && matcher.group().equals("/*")) {
                commentedIndices.add(matcher.start());

            } else if (matcher.group().equals("*/")) {
                commentedIndices.add(matcher.end());
            }
        }

        // Prevent an IndexOutOfBoundsException by just returning right now if no comments are found
        if (commentedIndices.size() == 0) {
            return source;
        }

        // If the comment indices list is odd, that means that there is an unclosed multi-line comment within the source
        // code. The end index of the comment should be the end of the source code string.
        if (commentedIndices.size() % 2 != 0) {
            commentedIndices.add(source.length());
        }

        // --- PART 3: Exclude the comments from the new source code ---
        StringBuilder noComments = new StringBuilder(source.substring(0, commentedIndices.get(0)));

        for (int i = 1; i < commentedIndices.size(); i += 2) {
            if (i + 1 < commentedIndices.size()) {
                noComments.append(source, commentedIndices.get(i), commentedIndices.get(i + 1));
            } else {
                // Avoid an IndexOutOfBoundsException by accessing an invalid index of commentedIndices
                noComments.append(source, commentedIndices.get(i), source.length());
            }
        }

        return noComments.toString();
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
