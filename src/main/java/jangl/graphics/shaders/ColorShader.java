package jangl.graphics.shaders;

import jangl.color.Color;

import java.util.Arrays;

import static org.lwjgl.opengl.GL46.glUniform4fv;

public class ColorShader extends Shader {
    private float[] rgba;

    private ColorShader() {
        super(
                Shader.class.getResourceAsStream("/shaders/colorShader/colorShader.vert"),
                Shader.class.getResourceAsStream("/shaders/colorShader/colorShader.frag")
        );
    }

    public ColorShader(Color color) {
        this();
        this.setRGBA(color);
    }

    @Deprecated
    public ColorShader(float red, float green, float blue, float alpha) {
        this();
        this.setRGBA(red, green, blue, alpha);
    }

    /**
     * @param rgba The RGBA value to set the color to
     * @throws IllegalArgumentException Throws if the RGBA length is not 4 (one value for r, g, b, and a)
     */
    @Deprecated
    public ColorShader(float[] rgba) throws IllegalArgumentException {
        this();
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

    /**
     * @param rgba The RGBA value to set the color to
     * @throws IllegalArgumentException Throws if the RGBA length is not 4 (one value for r, g, b, and a)
     */
    public void setRGBA(float[] rgba) throws IllegalArgumentException {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA length must be 4, not " + rgba.length);
        }

        this.rgba = Arrays.copyOf(rgba, rgba.length);
    }

    public void setRGBA(float red, float green, float blue, float alpha) {
        this.rgba = new float[]{red, green, blue, alpha};
    }

    public void setRGBA(Color color) {
        this.rgba = color.getNormRGBA();
    }
}
