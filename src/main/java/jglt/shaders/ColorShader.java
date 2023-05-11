package jglt.shaders;

import static org.lwjgl.opengl.GL20.glUniform4f;

public class ColorShader extends Shader {
    public final float red;
    public final float green;
    public final float blue;
    public final float alpha;

    public ColorShader(float red, float green, float blue, float alpha) {
        super(
                "src/main/resources/shaders/colorShader/colorShader.vert",
                "src/main/resources/shaders/colorShader/colorShader.frag"
        );

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public void bind() throws RuntimeException {
        super.bind();

        int colorUniformLocation = this.getUniformLocation("color");

        // This shouldn't happen but if it does
        if (colorUniformLocation == -1) {
            throw new RuntimeException("Could not find variable \"color\" in color shader");
        }

        glUniform4f(colorUniformLocation, this.red, this.green, this.blue, this.alpha);
    }
}
