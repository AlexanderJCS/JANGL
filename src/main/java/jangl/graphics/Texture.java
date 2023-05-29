package jangl.graphics;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL46.*;

/**
 * This class allows images to be drawn to the screen.
 * To use it, run Texture.bind() then render the TexturedModel you want to map it to.
 * <p>
 * Taken from this tutorial:
 * <a href="https://www.youtube.com/watch?v=crOzRjzqI-o&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=4&ab_channel=WarmfulDevelopment">...</a>
 */
public class Texture implements AutoCloseable {
    private final int id;
    public final int width;
    public final int height;

    /**
     * @param filepath   The filepath of the texture.
     * @param filterMode The filter mode for scaling the image. Common filter modes are:
     *                   GL_NEAREST, GL_LINEAR, GL_LINEAR_MIPMAP_LINEAR, etc. This depends on the effect you are going
     *                   for when scaling.
     *
     * @throws UncheckedIOException If the specified filepath cannot be found.
     */
    public Texture(String filepath, int filterMode) throws UncheckedIOException {
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();

        int[] rawData = bufferedImage.getRGB(0, 0, this.width, this.height, null, 0, this.width);
        ByteBuffer imageData = this.calculateImageData(rawData);

        this.id = this.createImage(imageData, width, height, filterMode);
    }

    /**
     * @param filepath The filepath of the texture. Defaults to nearest-neighbor filter mode.
     */
    public Texture(String filepath) throws UncheckedIOException {
        this(filepath, GL_NEAREST);
    }

    /**
     * @param filepath   The filepath of the image.
     * @param x          The x component, in pixels, of the top left corner of the rectangular region of the image to get
     * @param y          The y component, in pixels, of the top left corner of the rectangular region of the image to get
     * @param width      The width of the region, in pixels, to get.
     * @param height     The height of the region, in pixels, to get.
     * @param filterMode The filter mode for scaling the image. Common filter modes are:
     *                   GL_NEAREST, GL_LINEAR, GL_LINEAR_MIPMAP_LINEAR, etc. This depends on the effect you are going
     *                   for when scaling.
     * @throws IndexOutOfBoundsException Throws if the specified rectangle goes off the image
     * @throws UncheckedIOException If the specified filepath cannot be found.
     */
    public Texture(String filepath, int x, int y, int width, int height, int filterMode)
            throws IndexOutOfBoundsException, UncheckedIOException {
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(new File(filepath));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        this.width = width;
        this.height = height;

        int[] rawData = bufferedImage.getRGB(x, y, width, height, null, 0, width);
        ByteBuffer imageData = this.calculateImageData(rawData);

        this.id = this.createImage(imageData, width, height, filterMode);
    }

    /**
     * Defaults to nearest-neighbor filter mode.
     *
     * @param filepath The filepath of the image.
     * @param x        The x component, in pixels, of the top left corner of the rectangular region of the image to get
     * @param y        The y component, in pixels, of the top left corner of the rectangular region of the image to get
     * @param width    The width of the region, in pixels, to get.
     * @param height   The height of the region, in pixels, to get.
     *
     * @throws IndexOutOfBoundsException Throws IndexOutOfBoundsException if the specified rectangle goes off the image
     * @throws UncheckedIOException If the specified filepath cannot be found.
     */
    public Texture(String filepath, int x, int y, int width, int height)
            throws IndexOutOfBoundsException, UncheckedIOException {
        this(filepath, x, y, width, height, GL_NEAREST);
    }

    /**
     * Generates an image from an RGBA array of raw data (one int = 1 pixel). Mainly used for the Font class.
     *
     * @param bufferedImage The buffered image.
     * @param x             The top left x coordinate
     * @param y             The top left y coordinate
     * @param width         The width of the image
     * @param height        The height of the image
     * @param filterMode    The OpenGL filter mode.
     *
     * @throws IndexOutOfBoundsException Throws IndexOutOfBoundsException if the specified rectangle goes off the image
     * @throws UncheckedIOException If the specified filepath cannot be found.
     */
    public Texture(BufferedImage bufferedImage, int x, int y, int width, int height, int filterMode)
            throws IndexOutOfBoundsException, UncheckedIOException {

        int[] rawData = bufferedImage.getRGB(x, y, width, height, null, 0, width);
        ByteBuffer imageData = this.calculateImageData(rawData);
        this.id = this.createImage(imageData, width, height, filterMode);

        this.width = width;
        this.height = height;
    }

    /**
     * Unbinds any existing bound texture
     */
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    protected void putPixel(ByteBuffer buffer, int pixelData) {
        buffer.put((byte) ((pixelData >> 16) & 0xFF));  // Red
        buffer.put((byte) ((pixelData >> 8) & 0xFF));   // Green
        buffer.put((byte) (pixelData & 0xFF));          // Blue
        buffer.put((byte) ((pixelData >> 24) & 0xFF));  // Alpha
    }

    /**
     * Writes the designated region of image data to BufferManager.BYTE_BUFFER
     *
     * @param rawData The raw image data
     * @param width   The width of the region to get
     * @param height  The height of the region to get
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

    /**
     * @return the ID of the created image
     */
    private int createImage(ByteBuffer imageData, int width, int height, int filterMode) {
        int imageID = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, imageID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMode);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMode);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
        Texture.unbind();

        return imageID;
    }

    /**
     * Run this method before running TexturedModel.draw(). This will overlay the texture
     * on the TexturedModel.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    @Override
    public void close() {
        glDeleteTextures(this.id);
    }
}
