package jangl.graphics.shaders.premade;

import jangl.color.Color;
import jangl.graphics.shaders.Shader;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.ShaderType;

import static org.lwjgl.opengl.GL46.glUniform4fv;

public class ColorShader extends ShaderProgram {
    private Color color;

    public ColorShader(Color color) {
        super(
                new Shader(
                        ShaderProgram.class.getResourceAsStream("/shaders/colorShader/colorShader.frag"),
                        ShaderType.FRAGMENT
                )
        );

        this.setColor(color);
    }

    @Override
    public void bind() throws RuntimeException {
        super.bind();

        int colorUniformLocation = this.getUniformLocation("color");

        // This shouldn't happen but if it does
        if (colorUniformLocation == -1) {
            throw new RuntimeException("Could not find variable \"color\" in color shader");
        }

        glUniform4fv(colorUniformLocation, this.color.getNormRGBA());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
