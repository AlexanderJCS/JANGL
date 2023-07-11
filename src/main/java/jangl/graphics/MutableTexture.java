package jangl.graphics;

import jangl.color.Color;
import jangl.util.ArrayUtils;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

import java.awt.image.BufferedImage;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

/**
 * Allows you to set individual pixel values of the texture.
 */
public class MutableTexture extends Texture {
    private final ByteBuffer pixelBuffer;

    public MutableTexture(TextureBuilder builder) {
        super(builder);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    /**
     * Functionally the same as the Color color, int width, int height, int filterMode constructor,
     * but it has a default filter mode of GL_LINEAR.
     * @param color The color of the background
     * @param width The width of the texture in pixels
     * @param height The height of the texture in pixels
     */
    @Deprecated
    public MutableTexture(Color color, int width, int height, boolean obeyCamera) {
        this(color, width, height, GL_LINEAR, obeyCamera);
    }

    // TODO: fix broken docstring
    /**
     * but it has a default filter mode of GL_LINEAR.
     * @param color The color of the background
     * @param width The width of the texture in pixels
     * @param height The height of the texture in pixels
     * @param filterMode The filter mode of the texture.
     */
    @Deprecated

    public MutableTexture(Color color, int width, int height, int filterMode, boolean obeyCamera) {
        super(
                ArrayUtils.repeatSequence(ArrayUtils.intsToBytes(color.get255RGBA()), width * height),
                width, height, filterMode, obeyCamera
        );

        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    @Deprecated
    public MutableTexture(String filepath, int filterMode, boolean obeyCamera) throws UncheckedIOException {
        super(filepath, filterMode, obeyCamera);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    @Deprecated
    public MutableTexture(String filepath, boolean obeyCamera) throws UncheckedIOException {
        super(filepath, obeyCamera);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    @Deprecated
    public MutableTexture(String filepath, int x, int y, int width, int height, int filterMode, boolean obeyCamera)
            throws IndexOutOfBoundsException, UncheckedIOException
    {
        super(filepath, x, y, width, height, filterMode, obeyCamera);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    @Deprecated
    public MutableTexture(String filepath, int x, int y, int width, int height, boolean obeyCamera)
            throws IndexOutOfBoundsException, UncheckedIOException
    {
        super(filepath, x, y, width, height, obeyCamera);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    @Deprecated
    public MutableTexture(BufferedImage bufferedImage, int x, int y, int width, int height, int filterMode, boolean obeyCamera)
            throws IndexOutOfBoundsException, UncheckedIOException
    {
        super(bufferedImage, x, y, width, height, filterMode, obeyCamera);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    public void setPixelAt(int x, int y, Color color) {
        this.pixelBuffer.put(ArrayUtils.intsToBytes(color.get255RGBA()));
        this.pixelBuffer.flip();

        this.bind();
        glTexSubImage2D(
                GL_TEXTURE_2D,
                0, x, y,
                1, 1,
                GL_RGBA, GL_UNSIGNED_BYTE,
                this.pixelBuffer
        );

        Texture.unbind();
    }

    public void fillImage(Color color) {
        ByteBuffer imageBuffer = BufferUtils.createByteBuffer(this.width * this.height * 4);

        for (int i = 0; i < this.width * this.height; i++) {
            imageBuffer.put(ArrayUtils.intsToBytes(color.get255RGBA()));
        }

        imageBuffer.flip();
        this.bind();

        glTexSubImage2D(
                GL_TEXTURE_2D,
                0, 0, 0,
                this.width, this.height,
                GL_RGBA, GL_UNSIGNED_BYTE,
                imageBuffer
        );

        Texture.unbind();
    }
}
