package imageguide;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.graphics.textures.Image;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Rect;

public class ImageGuide {
    private final Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new WorldCoords(0.5f, 0.5f), 0.3f, 0.3f
                ),

                new Texture(
                        new TextureBuilder().setImagePath("src/guideCode/guideResources/imageGuide/image.png")
                )
        );
    }

    public void draw() {
        Window.clear();

        // Draw the image
        this.image.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            JANGL.update();
        }

        this.image.rect().close();
        this.image.texture().close();
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        Window.setVsync(true);

        new ImageGuide().run();

        Window.close();
    }
}