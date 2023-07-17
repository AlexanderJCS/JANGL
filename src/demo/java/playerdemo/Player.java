package playerdemo;

import jangl.coords.PixelCoords;
import jangl.coords.NDCoords;
import jangl.graphics.Image;
import jangl.graphics.Texture;
import jangl.graphics.TextureBuilder;
import jangl.io.keyboard.Keyboard;
import jangl.shapes.Rect;
import jangl.time.Clock;
import org.lwjgl.glfw.GLFW;

public class Player implements AutoCloseable {
    private final float speedX;
    private final float speedY;
    private final Image image;

    public Player(NDCoords coords, float speed) {
        this.speedX = PixelCoords.distXtoNDC(speed);
        this.speedY = PixelCoords.distYtoNDC(speed);

        this.image = new Image(
                new Rect(coords, PixelCoords.distXtoNDC(50), PixelCoords.distYtoNDC(50)),
                new Texture(new TextureBuilder().setImagePath("src/demo/demoResources/playerDemo/player.png").setObeyCamera(true))
        );
    }

    public void update() {
        float xDist = (float) (this.speedX * Clock.getTimeDelta());
        float yDist = (float) (this.speedY * Clock.getTimeDelta());
        Rect rect = this.image.rect();

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_W)) {
            rect.getTransform().shift(0, yDist);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_S)) {
            rect.getTransform().shift(0, -yDist);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_D)) {
            rect.getTransform().shift(xDist, 0);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_A)) {
            rect.getTransform().shift(-xDist, 0);
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
