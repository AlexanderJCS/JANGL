package jangl.graphics.shaders;

import java.io.*;

public class Shader {
    public final String sourceCode;
    public final int type;

    public Shader(String filepath, ShaderType shaderType) throws UncheckedIOException {
        this.sourceCode = precompile(loadShader(filepath));
        this.type = ShaderType.toOpenGLType(shaderType);
    }

    public Shader(InputStream shaderStream, ShaderType shaderType) throws UncheckedIOException {
        this.sourceCode = precompile(loadShader(shaderStream));
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

    private static String precompile(String source) {
        String[] splitLines = source.split("\n");

        for (int i = 0; i < splitLines.length; i++) {
            String line = splitLines[i];

            if (!line.startsWith("#include")) {
                continue;
            }

            int firstQuoteIndex = line.indexOf('"');
            int secondQuoteIndex = line.indexOf('"', firstQuoteIndex + 1);

            if (firstQuoteIndex == -1 || secondQuoteIndex == -1) {
                throw new ShaderCompileException("Precompilation error: Could not parse line\n" + line);
            }

            String fileToImport = line.substring(firstQuoteIndex + 1, secondQuoteIndex);

            InputStream stream = Shader.class.getResourceAsStream("/shaderInclude/" + fileToImport);
            String importedFile = loadShader(stream);

            importedFile += "\n#line " + (i + 2);  // set the line number back to what it should be

            splitLines[i] = importedFile;
        }

        return String.join("\n", splitLines);
    }
}
