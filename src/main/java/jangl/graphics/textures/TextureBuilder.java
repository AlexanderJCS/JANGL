package jangl.graphics.textures;

import jangl.color.Color;
import jangl.util.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL46;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

public class TextureBuilder {
    private int filterMode;
    private int x;
    private int y;
    private int width;
    private int height;
    private ByteBuffer imageData;
    private boolean obeyCamera;

    public TextureBuilder() {
        this.filterMode = GL46.GL_NEAREST;
        this.x = 0;
        this.y = 0;

        // set to -1 as a "default" value. If it's set as -1 then that means that it should be the max image dimensions
        this.width = -1;
        this.height = -1;

        this.obeyCamera = true;
    }

    public int getFilterMode() {
        return filterMode;
    }

    public TextureBuilder setFilterMode(int filterMode) {
        this.filterMode = filterMode;
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
        return width;
    }

    public TextureBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public TextureBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public boolean isObeyingCamera() {
        return obeyCamera;
    }

    public void setObeyCamera(boolean obeyCamera) {
        this.obeyCamera = obeyCamera;
    }

    public ByteBuffer getImageData() {
        return imageData;
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
