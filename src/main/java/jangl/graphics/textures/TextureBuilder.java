package jangl.graphics.textures;

import jangl.color.Color;
import jangl.util.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL41.GL_NEAREST;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11C.GL_REPEAT;

public class TextureBuilder {
    private int filterMode;
    private int x;
    private int y;
    private int width;
    private int height;
    private ByteBuffer imageData;
    private boolean obeyCamera;
    private int wrapMode;

    public TextureBuilder() {
        this.x = 0;
        this.y = 0;

        // set to -1 as a "default" value. If it's set as -1 then that means that it should be the max image dimensions
        this.width = -1;
        this.height = -1;

        this.obeyCamera = true;

        this.setSmoothScaling();
        this.setWrapMode(GL_REPEAT);
    }

    public int getFilterMode() {
        return filterMode;
    }

    /**
     * Sets the OpenGL filter mode.
     * @param filterMode The OpenGL filter mode.
     * @return This object, to allow for method chaining.
     */
    public TextureBuilder setFilterMode(int filterMode) {
        this.filterMode = filterMode;
        return this;
    }

    /**
     * The default option for TextureBuilder. When scaling, the texture is optimized for scaling higher-resolution
     * images, but may make low-resolution images such as pixel art blurry. On a higher resolution texture,
     * it will make the image look smoother and more seamless.
     * <br>
     * Under the hood, this method changes the filter mode to GL_LINEAR.
     *
     * @return This object, to allow for method chaining
     */
    public TextureBuilder setSmoothScaling() {
        this.setFilterMode(GL_LINEAR);

        return this;
    }

    /**
     * When scaling, the texture is optimized for scaling pixelated images, such
     * as pixel art. It will not make the image blurry when scaling, but may make higher-resolution images look rough.
     * <br>
     * Under the hood, this method changes the filter mode to GL_NEAREST.
     *
     * @return This object, to allow for method chaining
     */
    public TextureBuilder setPixelatedScaling() {
        this.setFilterMode(GL_NEAREST);

        return this;
    }

    public int getX() {
        return x;
    }

    public TextureBuilder setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public TextureBuilder setY(int y) {
        this.y = y;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    /**
     * Sets the width of the image. This method does not change the image data, but instead only changes the dimensions
     * given to OpenGL, so this method should only be called if you are manually specifying the image data.
     *
     * @param width The width of the image.
     * @return This object, to allow for method chaining.
     * @throws IllegalArgumentException Throws when the width value given is less than or equal to 0
     */
    public TextureBuilder setWidth(int width) throws IllegalArgumentException {
        if (width <= 0) {
            throw new IllegalArgumentException("Width value must be greater than 0");
        }

        this.width = width;
        return this;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * Sets the height of the image. This method does not change the image data, but instead only changes the dimensions
     * given to OpenGL, so this method should only be called if you are manually specifying the image data.
     *
     * @param height The height of the image.
     * @return This object, to allow for method chaining.
     * @throws IllegalArgumentException Throws when the height value given is less than or equal to 0
     */
    public TextureBuilder setHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height value must be greater than 0");
        }

        this.height = height;
        return this;
    }

    public boolean isObeyingCamera() {
        return this.obeyCamera;
    }

    public TextureBuilder setObeyCamera(boolean obeyCamera) {
        this.obeyCamera = obeyCamera;

        return this;
    }

    public ByteBuffer getImageData() {
        return this.imageData;
    }

    public TextureBuilder setImageData(ByteBuffer imageData) {
        this.imageData = imageData;

        return this;
    }

    /**
     * Fills the texture with a certain color, width, and height.
     *
     * @param color  The color of the image to set to
     * @param width  The width of the image
     * @param height The height of the image
     */
    public TextureBuilder fill(Color color, int width, int height) {
        this.imageData = BufferUtils.createByteBuffer(width * height * 4);
        this.imageData.put(
                ArrayUtils.repeatSequence(ArrayUtils.intsToBytes(color.get255RGBA()), width * height)
        );
        this.imageData.flip();

        this.width = width;
        this.height = height;

        return this;
    }

    /**
     * Sets the image via a png filepath. Warning: this will reset image width, height, x, and y. Make sure to set those
     * variables after calling this method.
     *
     * @param filepath The filepath of the png to read.
     */
    public TextureBuilder setImagePath(String filepath) {
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        this.x = 0;
        this.y = 0;
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();

        int[] rawData = bufferedImage.getRGB(this.x, this.y, this.width, this.height, null, 0, this.width);
        this.imageData = this.calculateImageData(rawData);

        return this;
    }

    /**
     * @param wrapMode The OpenGL wrap mode to use. E.g., GL_CLAMP_TO_EDGE, GL_REPEAT, etc
     * @return This object, to allow method chaining
     */
    public TextureBuilder setWrapMode(int wrapMode) {
        this.wrapMode = wrapMode;
        return this;
    }

    public int getWrapMode() {
        return this.wrapMode;
    }

    protected void putPixel(ByteBuffer buffer, int pixelData) {
        buffer.put((byte) ((pixelData >> 16) & 0xFF));  // Red
        buffer.put((byte) ((pixelData >> 8) & 0xFF));   // Green
        buffer.put((byte) (pixelData & 0xFF));          // Blue
        buffer.put((byte) ((pixelData >> 24) & 0xFF));  // Alpha
    }

    /**
     * Writes the designated region of image data to byte buffer
     *
     * @param rawData The raw image data
     */
    protected ByteBuffer calculateImageData(int[] rawData) {
        ByteBuffer imageData = BufferUtils.createByteBuffer(this.width * this.height * 4);

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                int pixel = rawData[i * this.width + j];
                this.putPixel(imageData, pixel);
            }
        }

        imageData.flip();

        return imageData;
    }

    public GLFWImage toGLFWImage() {
        GLFWImage image = GLFWImage.create();
        image.width(this.width);
        image.height(this.height);
        image.pixels(this.imageData);

        return image;
    }

    public GLFWImage.Buffer toGLFWImageBuffer() {
        GLFWImage.Buffer imageBuffer = GLFWImage.create(1);
        imageBuffer.put(0, this.toGLFWImage());

        return imageBuffer;
    }
}
