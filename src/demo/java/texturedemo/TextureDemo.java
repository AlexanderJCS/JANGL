package texturedemo;

import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.graphics.Texture;
import jangl.io.Window;
import jangl.shapes.Rect;

public class TextureDemo implements AutoCloseable {
    private final Rect rect;
    private final Texture texture;

    public TextureDemo() {
        this.rect = new Rect(new NDCoords(-0.5f, 0.5f), 1, 1);
        this.texture = new Texture("src/demo/demoResources/textureDemo/image.png", true);
    }

    public void draw() {
        Window.clear();

        this.rect.draw(this.texture);
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
