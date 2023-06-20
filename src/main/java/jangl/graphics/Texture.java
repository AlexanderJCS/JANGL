package jangl.graphics;

import jangl.graphics.shaders.AttribLocation;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.TextureShaderFrag;
import jangl.graphics.shaders.premade.TextureShaderVert;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;

/**
 * This class allows images to be drawn to the screen.
 * To use it, run Texture.bind() then render the TexturedModel you want to map it to.
 */
public class Texture implements AutoCloseable {
    private final int id;
    private final ShaderProgram shaderProgram;
    public final int width;
    public final int height;
    private boolean useDefaultShader = true;

    /**
     * Creates a texture from the raw data.
     *
     * @param rawData    The raw texture data. Should be of length width * height * 4.
     * @param width      The width of the image.
     * @param height     The height of the image.
     * @param filterMode The OpenGL filter mode ID.
     */
    protected Texture(byte[] rawData, int width, int height, int filterMode, boolean obeyCamera) {
        this.width = width;
        this.height = height;

        ByteBuffer imageData = BufferUtils.createByteBuffer(width * height * 4);
        imageData.put(rawData);
        imageData.flip();

        this.id = this.createImage(imageData, this.width, this.height, filterMode);
        this.shaderProgram = createShader(obeyCamera);
    }

    /**
     * @param filepath   The filepath of the texture.
     * @param filterMode The filter mode for scaling the image. Common filter modes are:
     *                   GL_NEAREST, GL_LINEAR, GL_LINEAR_MIPMAP_LINEAR, etc. This depends on the effect you are going
     *                   for when scaling.
     * @param obeyCamera True if the texture should move on the screen when the camera moves. False to stay stationary
     *                   when the camera moves.
     *
     * @throws UncheckedIOException If the specified filepath cannot be found.
     */
    public Texture(String filepath, int filterMode, boolean obeyCamera) throws UncheckedIOException {
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
        this.shaderProgram = createShader(obeyCamera);
    }

    /**
     * @param filepath The filepath of the texture. Defaults to nearest-neighbor filter mode.
     */
    public Texture(String filepath, boolean obeyCamera) throws UncheckedIOException {
        this(filepath, GL_NEAREST, obeyCamera);
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
    public Texture(String filepath, int x, int y, int width, int height, int filterMode, boolean obeyCamera)
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
        this.shaderProgram = createShader(obeyCamera);
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
    public Texture(String filepath, int x, int y, int width, int height, boolean obeyCamera)
            throws IndexOutOfBoundsException, UncheckedIOException {
        this(filepath, x, y, width, height, GL_NEAREST, obeyCamera);
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
    public Texture(BufferedImage bufferedImage, int x, int y, int width, int height, int filterMode, boolean obeyCamera)
            throws IndexOutOfBoundsException, UncheckedIOException {

        int[] rawData = bufferedImage.getRGB(x, y, width, height, null, 0, width);
        ByteBuffer imageData = this.calculateImageData(rawData);
        this.id = this.createImage(imageData, width, height, filterMode);

        this.width = width;
        this.height = height;
        this.shaderProgram = createShader(obeyCamera);
    }

    private static ShaderProgram createShader(boolean obeyCamera) {
        List<AttribLocation> attribLocations = Arrays.asList(
                new AttribLocation(0, "vertices"),
                new AttribLocation(1, "textures")
        );

        return new ShaderProgram(new TextureShaderVert(obeyCamera), new TextureShaderFrag(), attribLocations);
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
        if (this.useDefaultShader) {
            this.shaderProgram.bind();
        }

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    /**
     * Unbinds any existing bound texture
     */
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
        ShaderProgram.unbind();
    }

    /**
     * This method allows you to configure if the shader program attached to the texture (default shader) is used when
     * the texture is bound/unbound. Only set this to false if you plan on using your own custom shader in place of
     * the default.
     *
     * @param useDefaultShader True to use the default shader. False to use a custom shader.
     */
    public void useDefaultShader(boolean useDefaultShader) {
        this.useDefaultShader = useDefaultShader;
    }

    @Override
    public void close() {
        glDeleteTextures(this.id);
        this.shaderProgram.close();
    }
}
