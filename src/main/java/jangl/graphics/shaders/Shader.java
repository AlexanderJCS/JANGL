package jangl.graphics.shaders;

import io.github.pixee.security.BoundedLineReader;
import jangl.graphics.shaders.exceptions.ShaderPrecompileException;

import java.io.*;

public class Shader {
    public final String sourceCode;

    /**
     * Load a shader program.
     *
     * @param filepath The filepath to the shader program.
     * @throws UncheckedIOException Throws an UncheckedIOException if it cannot find or read the file.
     */
    public Shader(String filepath) throws UncheckedIOException {
        this.sourceCode = this.precompile(loadShader(filepath));
    }

    /**
     * Load a shader program.
     *
     * @param shaderStream The stream of the shader program code.
     * @throws UncheckedIOException Throws an UncheckedIOException if it cannot read the stream.
     */
    public Shader(InputStream shaderStream) throws UncheckedIOException {
        this.sourceCode = this.precompile(loadShader(shaderStream));
    }

    private static String loadShader(InputStream inputStream) throws UncheckedIOException {
        // Slightly modified solution 8 from:
        // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java

        String newLine = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder result = new StringBuilder();

        try {
            for (String line; (line = BoundedLineReader.readLine(reader, 5_000_000)) != null;) {
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
            while ((line = BoundedLineReader.readLine(reader, 5_000_000)) != null) {
                shaderSource.append(line).append("\n");
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return shaderSource.toString();
    }

    /**
     * Override this method to pass uniforms to the shader or do any calculation before the shader runs.
     * To pass a uniform, use:<br>
     * int location = org.lwjgl.opengl.GL41.glGetUniformLocation(programID, "uniform name");
     * org.lwjgl.opengl.GL41.glUniform___(location, data);
     *
     * @param programID The ID of the program. Used for passing uniform information.
     */
    public void setUniforms(int programID) {

    }

    /**
     * Precompile the shader. Right now, it just adds #includes.
     *
     * @param source The source code of the shader.
     * @return The source code of the shader with other #included code added in
     */
    protected String precompile(String source) {
        String[] splitLines = source.split("\n");

        for (int i = 0; i < splitLines.length; i++) {
            String line = splitLines[i];

            if (!line.startsWith("#include")) {
                continue;
            }

            int firstQuoteIndex = line.indexOf('"');
            int secondQuoteIndex = line.indexOf('"', firstQuoteIndex + 1);

            if (firstQuoteIndex == -1 || secondQuoteIndex == -1) {
                throw new ShaderPrecompileException("Could not precompile shader:\nError parsing line:\n (" + (i + 1) + ") " + line);
            }

            String fileToImport = line.substring(firstQuoteIndex + 1, secondQuoteIndex);

            InputStream stream = Shader.class.getResourceAsStream("/shaderInclude/" + fileToImport);

            if (stream == null) {
                throw new ShaderPrecompileException("Could not precompile shader:\nCould not find file /shaderInclude/" + fileToImport + "\n (" + (i + 1) + ") " + line);
            }

            String importedFile = loadShader(stream);

            importedFile += "\n#line " + (i + 2);  // set the line number back to what it should be

            splitLines[i] = importedFile;
        }

        return String.join("\n", splitLines);
    }
}
