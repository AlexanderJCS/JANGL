package playerdemo;

import jangl.coords.WorldCoords;
import jangl.graphics.textures.Image;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.keyboard.Keyboard;
import jangl.shapes.Rect;
import jangl.time.Clock;
import org.lwjgl.glfw.GLFW;

public class Player implements AutoCloseable {
    private final float speed;
    private final Image image;

    public Player(WorldCoords coords, float speed) {
        this.speed = speed;
        this.image = new Image(
                new Rect(coords, 0.07f, 0.07f),
                new Texture(new TextureBuilder().setImagePath("src/demo/demoResources/playerDemo/player.png"))
        );
    }

    public void update() {
        float dist = (float) (this.speed * Clock.getTimeDelta());
        Rect rect = this.image.rect();

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_W)) {
            rect.getTransform().shift(0, dist);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_S)) {
            rect.getTransform().shift(0, -dist);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_D)) {
            rect.getTransform().shift(dist, 0);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_A)) {
            rect.getTransform().shift(-dist, 0);
        }
    }

    public void draw() {
        this.image.draw();
    }

    @Override
    public void close() {
        this.image.rect().close();
        this.image.texture().close();
    }
}
