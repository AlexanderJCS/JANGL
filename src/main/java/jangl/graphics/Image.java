package jangl.graphics;

import jangl.shapes.Rect;

/**
 * An image record, which has a rect and a texture to bind onto the rectangle when drawn.
 */
public record Image(Rect rect, Texture texture) {
    public void draw() {
        this.rect.draw(this.texture);
    }

    /**
     * @return A reference to the image's rect
     */
    @Override
    public Rect rect() {
        return this.rect;
    }

    /**
     * @return A reference to the image's texture
     */
    @Override
    public Texture texture() {
        return this.texture;
    }
}
