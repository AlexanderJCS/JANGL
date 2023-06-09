package jangl.graphics.shaders.premade;

import jangl.color.Color;
import jangl.graphics.shaders.FragmentShader;

import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4fv;

/**
 * Use this shader color an object a specific color.
 */
public class ColorShader extends FragmentShader {
    private Color color;

    public ColorShader(Color color) throws UncheckedIOException {
        super(ColorShader.class.getResourceAsStream("/shaders/colorShader/colorShader.frag"));
        this.color = color;
    }

    @Override
    public void setUniforms(int programID) {
        int colorUniformLocation = glGetUniformLocation(programID, "color");

        // This shouldn't happen but if it does
        if (colorUniformLocation == -1) {
            throw new RuntimeException("Could not find variable \"color\" in color shader");
        }

        glUniform4fv(colorUniformLocation, this.color.getNormRGBA());
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
