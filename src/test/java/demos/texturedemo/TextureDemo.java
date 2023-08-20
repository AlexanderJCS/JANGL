package demos.texturedemo;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Triangle;

public class TextureDemo implements AutoCloseable {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;
    private final Texture texture;

    public TextureDemo() {
        this.rect = new Rect(new WorldCoords(0.8f, 0.6f), 0.25f, 0.25f);
        this.circle = new Circle(new WorldCoords(1.3f, 0.5f), 0.1f, 70);
        this.triangle = new Triangle(new WorldCoords(0.3f, 0.3f), new WorldCoords(0.8f, 0.3f), new WorldCoords(0.5f, 0.6f));

        this.rect.setTexRepeatX(2);
        this.rect.setTexRepeatY(2);

        this.texture = new Texture(
                new TextureBuilder().setImagePath("src/demo/demoResources/textureDemo/image.png")
        );
    }

    public void draw() {
        Window.clear();

        this.rect.draw(this.texture);
        this.circle.draw(this.texture);
        this.triangle.draw(this.texture);
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            JANGL.update();
        }
    }

    @Override
    public void close() {
        this.rect.close();
        this.circle.close();
        this.triangle.close();
        this.texture.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);

        TextureDemo textureDemo = new TextureDemo();
        textureDemo.run();
        textureDemo.close();

        Window.close();
    }
}
