package jangl.graphics;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL21.*;

/**
 * This class allows images to be drawn to the screen.
 * To use it, run Texture.bind() then render the TexturedModel you want to map it to.
 * <p>
 * Taken from this tutorial:
 * <a href="https://www.youtube.com/watch?v=crOzRjzqI-o&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=4&ab_channel=WarmfulDevelopment">...</a>
 */
public class Texture implements AutoCloseable {
    private final int id;

    /**
     * Creates a new Texture object.
     *
     * @param filepath The filepath of the texture.
     */

    public Texture(String filepath) throws UncheckedIOException {
        try {
            BufferManager.bufferedImage = ImageIO.read(new File(filepath));
            int width = BufferManager.bufferedImage.getWidth();
            int height = BufferManager.bufferedImage.getHeight();

            int[] pixels_raw;
            pixels_raw = BufferManager.bufferedImage.getRGB(0, 0, width, height, null, 0, width);

            // Create the pixel buffer
            BufferManager.BYTE_BUFFER.clear();
            BufferManager.BYTE_BUFFER.limit(width * height * 4);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = pixels_raw[i * width + j];
                    BufferManager.BYTE_BUFFER.put((byte) ((pixel >> 16) & 0xFF));  // Red
                    BufferManager.BYTE_BUFFER.put((byte) ((pixel >> 8) & 0xFF));   // Green
                    BufferManager.BYTE_BUFFER.put((byte) (pixel & 0xFF));          // Blue
                    BufferManager.BYTE_BUFFER.put((byte) ((pixel >> 24) & 0xFF));  // Alpha
                }
            }

            BufferManager.BYTE_BUFFER.flip();
            id = glGenTextures();
            this.bind();

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferManager.BYTE_BUFFER);
            Texture.unbind();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Run this method before running TexturedModel.draw(). This will overlay the texture
     * on the TexturedModel.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, this.id);
    }

    /**
     * Unbinds any existing bound texture
     */
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public void close() {
        glDeleteTextures(this.id);
    }
}
