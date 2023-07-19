package jangl.graphics.textures;

import jangl.color.Color;
import jangl.util.ArrayUtils;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL46.*;

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

        this.unbind();
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

        this.unbind();
    }
}
