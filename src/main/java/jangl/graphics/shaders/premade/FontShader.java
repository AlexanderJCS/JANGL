package jangl.graphics.shaders.premade;

import jangl.color.Color;
import jangl.graphics.shaders.FragmentShader;

import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL20.*;

/**
 * The shader used to set fonts to any color.
 */
public class FontShader extends FragmentShader {
    private Color color;
    private boolean keepDefaultColors;

    public FontShader(Color color) throws UncheckedIOException {
        super(FontShader.class.getResourceAsStream("/shaders/fontShader/fontShader.frag"));

        this.color = color;
        this.keepDefaultColors = false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isKeepingDefaultColors() {
        return this.keepDefaultColors;
    }

    public void setKeepDefaultColors(boolean keepDefaultColors) {
        this.keepDefaultColors = keepDefaultColors;
    }

    @Override
    public void setUniforms(int programID) {
        int colorLocation = glGetUniformLocation(programID, "color");
        glUniform4fv(colorLocation, this.color.getNormRGBA());

        int samplerLocation = glGetUniformLocation(programID, "texSampler");
        glUniform1i(samplerLocation, 0);

        int defaultColorLocation = glGetUniformLocation(programID, "keepDefaultColors");
        glUniform1i(defaultColorLocation, this.keepDefaultColors ? 1 : 0);
    }
}
