package guides.imageguide;

import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.graphics.textures.enums.WrapMode;
import jangl.io.Window;
import jangl.shapes.Rect;

public class ImageGuide {
    private final Rect rect;
    private final Texture texture;

    public ImageGuide() {
        this.rect = new Rect(
                        new WorldCoords(0.5f, 0.5f), 0.3f, 0.3f
        );

        this.rect.setTexRepeatX(2);
        this.rect.setTexRepeatY(2);

        this.texture = new Texture(
                new TextureBuilder()
                        .setImagePath("src/test/resources/guide/imageGuide/image.png")
                        .setWrapMode(WrapMode.REPEAT)
                        .setPixelatedScaling()
        );
    }

    public void draw() {
        Window.clear();

        // Draw the image
        this.rect.draw(this.texture);
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            Jangl.update();
        }

        this.rect.close();
        this.texture.close();
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new ImageGuide().run();

        Window.close();
    }
}