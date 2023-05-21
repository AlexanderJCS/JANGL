package jangl.graphics.shaders;

import java.io.*;
import java.util.Scanner;

import static org.lwjgl.opengl.GL46.*;

public class Shader implements AutoCloseable {
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    /**
     * @param vertexFile   The filepath to the vertex shader.
     * @param fragmentFile The filepath to the fragment shader.
     */
    public Shader(String vertexFile, String fragmentFile) {
        this.vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
        this.fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);

        this.programID = glCreateProgram();

        glAttachShader(this.programID, this.vertexShaderID);
        glAttachShader(this.programID, this.fragmentShaderID);
        glLinkProgram(this.programID);
        glValidateProgram(this.programID);
    }

    public Shader(InputStream vertexStream, InputStream fragmentStream) {
        // https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
        Scanner vertexScanner = new Scanner(vertexStream).useDelimiter("\\A");
        String vertexSource = vertexScanner.hasNext() ? vertexScanner.next() : "";
        vertexScanner.close();

        Scanner fragmentScanner = new Scanner(fragmentStream).useDelimiter("\\A");
        String fragmentSource = fragmentScanner.hasNext() ? fragmentScanner.next() : "";
        fragmentScanner.close();

        this.vertexShaderID = compileShader(vertexSource, GL_VERTEX_SHADER);
        this.fragmentShaderID = compileShader(fragmentSource, GL_FRAGMENT_SHADER);

        this.programID = glCreateProgram();

        glAttachShader(this.programID, this.vertexShaderID);
        glAttachShader(this.programID, this.fragmentShaderID);
        glLinkProgram(this.programID);
        glValidateProgram(this.programID);
    }

    /**
     * @param file The filepath to the shader.
     * @param type The type of shader. Either GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
     * @return The ID of the shader that was just loaded.
     */
    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return compileShader(shaderSource.toString(), type);
    }

    /**
     * @param program The shader program source code
     * @param type    The type of shader program (either GL_VERTEX_SHADER or GL_FRAGMENT_SHADER)
     * @return The ID of the compiled shader
     */
    private static int compileShader(String program, int type) {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, program);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(shaderID, 1024));
            System.err.println("Could not compile shader");
            System.exit(1);
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
    }

    /**
     * Get the location of a variable's name to be passed into OpenGL. This is used to pass uniform information
     * into the shader.
     *
     * @param varName The variable name.
     * @return The location of the shader to be passed into OpenGL.
     */
    public int getUniformLocation(String varName) {
        return glGetUniformLocation(this.programID, varName);
    }

    /**
     * Delete all information relating to the shader to avoid memory leaks.
     */
    @Override
    public void close() {
        Shader.unbind();

        glDetachShader(this.programID, this.vertexShaderID);
        glDetachShader(this.programID, this.fragmentShaderID);
        glDeleteShader(this.vertexShaderID);
        glDeleteShader(this.fragmentShaderID);
        glDeleteProgram(this.programID);
    }
}
