package demos.shaderdemo;

import jangl.color.Color;
import jangl.graphics.shaders.FragmentShader;
import org.lwjgl.opengl.GL41;

import java.io.UncheckedIOException;

public class CustomColorShader extends FragmentShader {
    private final Color color;

    public CustomColorShader(Color color) throws UncheckedIOException {
        super("src/demo/java/demos.shaderdemo/colorShader.frag");
        this.color = color;
    }

    @Override
    public void setUniforms(int programID) {
        int location = GL41.glGetUniformLocation(programID, "color");
        GL41.glUniform4fv(location, color.getNormRGBA());
    }
}
