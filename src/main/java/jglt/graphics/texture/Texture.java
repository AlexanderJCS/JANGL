package jglt.graphics.texture;

import jglt.graphics.BufferManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.lwjgl.opengl.GL21.*;

/**
 * This class allows images to be drawn to the screen.
 * To use it, run Texture.bind() then render the TexturedModel you want to map it to.
 * <p>
 * Taken from this tutorial:
 * <a href="https://www.youtube.com/watch?v=crOzRjzqI-o&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=4&ab_channel=WarmfulDevelopment">...</a>
 */
public class Texture implements AutoCloseable {
    private int id;

    /**
     * Creates a new Texture object.
     *
     * @param filepath The filepath of the texture.
     */

    public Texture(String filepath) {
        try {
            BufferManager.bufferedImage = ImageIO.read(new File(filepath));
            int width = BufferManager.bufferedImage.getWidth();
            int height = BufferManager.bufferedImage.getHeight();

            int[] pixels_raw;
            pixels_raw = BufferManager.bufferedImage.getRGB(0, 0, width, height, null, 0, width);

            // Create the pixel buffer
            BufferManager.byteBuffer.clear();
            BufferManager.byteBuffer.limit(width * height * 4);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = pixels_raw[i * width + j];
                    BufferManager.byteBuffer.put((byte) ((pixel >> 16) & 0xFF));  // Red
                    BufferManager.byteBuffer.put((byte) ((pixel >> 8) & 0xFF));   // Green
                    BufferManager.byteBuffer.put((byte) (pixel & 0xFF));          // Blue
                    BufferManager.byteBuffer.put((byte) ((pixel >> 24) & 0xFF));  // Alpha
                }
            }

            BufferManager.byteBuffer.flip();
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, this.id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferManager.byteBuffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
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
