package jangl.graphics;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

import java.awt.image.BufferedImage;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;

/**
 * Allows you to set individual pixel values of the texture. As a consequence, this will use more memory than an
 * immutable Texture, so it is recommended to use that whenever possible.
 */
public class MutableTexture extends Texture {
    private final ByteBuffer pixelBuffer;

    public MutableTexture(String filepath, int filterMode) throws UncheckedIOException {
        super(filepath, filterMode);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    public MutableTexture(String filepath) throws UncheckedIOException {
        super(filepath);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    public MutableTexture(String filepath, int x, int y, int width, int height, int filterMode) throws IndexOutOfBoundsException, UncheckedIOException {
        super(filepath, x, y, width, height, filterMode);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    public MutableTexture(String filepath, int x, int y, int width, int height) throws IndexOutOfBoundsException, UncheckedIOException {
        super(filepath, x, y, width, height);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    public MutableTexture(BufferedImage bufferedImage, int x, int y, int width, int height, int filterMode) throws IndexOutOfBoundsException, UncheckedIOException {
        super(bufferedImage, x, y, width, height, filterMode);
        this.pixelBuffer = BufferUtils.createByteBuffer(16);
    }

    public void setPixelAt(int x, int y, float[] rgba) throws IllegalArgumentException {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA array length != 4");
        }

        this.setPixelAt(x, y, rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public void setPixelAt(int x, int y, float red, float green, float blue, float alpha) {
        this.pixelBuffer.put((byte) (red * 255));
        this.pixelBuffer.put((byte) (green * 255));
        this.pixelBuffer.put((byte) (blue * 255));
        this.pixelBuffer.put((byte) (alpha * 255));

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
}
