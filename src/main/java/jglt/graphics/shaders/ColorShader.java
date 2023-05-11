package jglt.graphics.shaders;

import java.util.Arrays;

import static org.lwjgl.opengl.GL20.glUniform4fv;

public class ColorShader extends Shader {
    private float[] rgba;

    public ColorShader(float red, float green, float blue, float alpha) {
        super(
                "src/main/resources/shaders/colorShader/colorShader.vert",
                "src/main/resources/shaders/colorShader/colorShader.frag"
        );

        this.setRGBA(red, green, blue, alpha);
    }

    /**
     * @param rgba The RGBA value to set the color to
     * @throws IllegalArgumentException Throws if the RGBA length is not 4 (one value for r, g, b, and a)
     */
    public ColorShader(float[] rgba) throws IllegalArgumentException {
        super(
                "src/main/resources/shaders/colorShader/colorShader.vert",
                "src/main/resources/shaders/colorShader/colorShader.frag"
        );

        this.setRGBA(rgba);
    }

    @Override
    public void bind() throws RuntimeException {
        super.bind();

        int colorUniformLocation = this.getUniformLocation("color");

        // This shouldn't happen but if it does
        if (colorUniformLocation == -1) {
            throw new RuntimeException("Could not find variable \"color\" in color shader");
        }

        glUniform4fv(colorUniformLocation, this.rgba);
    }

    public float[] getRGBA() {
        return Arrays.copyOf(this.rgba, this.rgba.length);
    }

    public void setRGBA(float red, float green, float blue, float alpha) {
        this.rgba = new float[]{ red, green, blue, alpha };
    }

    /**
     * @param rgba The RGBA value to set the color to
     * @throws IllegalArgumentException Throws if the RGBA length is not 4 (one value for r, g, b, and a)
     */
    public void setRGBA(float[] rgba) throws IllegalArgumentException {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA length must be 4, not " + rgba.length);
        }

        this.rgba = rgba;
    }
}
