package jglt.graphics;

import jglt.graphics.texture.Texture;
import jglt.shapes.Rect;

public class Image implements AutoCloseable {
    private Rect rect;
    private Texture texture;

    public Image(Rect rect, Texture texture) {
        this.rect = rect;
        this.texture = texture;
    }

    public void draw() {
        this.texture.bind();
        this.rect.drawModel();
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
    public Texture texture() {
        return this.texture;
    }

    @Override
    public void close() {
        this.rect.close();
        this.texture.close();
    }
}
