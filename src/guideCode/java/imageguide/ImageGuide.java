package imageguide;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.coords.PixelCoords;
import jangl.graphics.textures.Image;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class ImageGuide {
    Image image;

    public ImageGuide() {
        this.image = new Image(
                new Rect(
                        new WorldCoords(0, 0),
                        PixelCoords.distToWorldCoords(300),
                        PixelCoords.distToWorldCoords(300)
                ),

                new Texture(
                        new TextureBuilder().setImagePath("/path/to/your/image/image.png")
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

            // Run the window at 60 fps
            try {
                Clock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.image.rect().close();
        this.image.texture().close();
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ImageGuide().run();
        Window.close();
    }
}