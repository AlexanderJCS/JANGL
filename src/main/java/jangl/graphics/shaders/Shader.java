package jangl.graphics.shaders;

import java.io.*;

public class Shader {
    public final String sourceCode;
    public final int type;

    public Shader(String filepath, ShaderType shaderType) throws UncheckedIOException {
        this.sourceCode = loadShader(filepath);
        this.type = ShaderType.toOpenGLType(shaderType);
    }

    public Shader(InputStream shaderStream, ShaderType shaderType) throws UncheckedIOException {
        this.sourceCode = loadShader(shaderStream);
        this.type = ShaderType.toOpenGLType(shaderType);
    }

    /**
     * The method to override to pass uniforms to the shader.
     *
     * @param programID The ID of the program. Used for passing uniform information.
     */
    public void setUniforms(int programID) {

    }

    private static String loadShader(InputStream inputStream) throws UncheckedIOException {
        // Slightly modified solution 8 from:
        // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java

        String newLine = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder result = new StringBuilder();

        try {
            for (String line; (line = reader.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append(newLine);
                }
                result.append(line);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return result.toString();
    }

    private static String loadShader(String filepath) throws UncheckedIOException {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));

            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return shaderSource.toString();
    }
}
