package jglt.graphics;

import jglt.shapes.Rect;

/**
 * An image class, which has a rect and a texture to bind onto the rectangle when drawn.
 */
public class Image implements AutoCloseable {
    private final Rect rect;
    private final Texture texture;

    public Image(Rect rect, Texture texture) {
        this.rect = rect;
        this.texture = texture;
    }

    public void draw() {
        this.texture.bind();
        this.rect.draw();
    }

    /**
     * @return A reference to the image's rect
     */
    public Rect getRect() {
        return this.rect;
    }

    /**
     * @return A reference to the image's texture
     */
    public Texture getTexture() {
        return this.texture;
    }

    /**
     * Prevents a memory leak, so it is essential to close the class after use
     */
    @Override
    public void close() {
        this.rect.close();
        this.texture.close();
    }
}
