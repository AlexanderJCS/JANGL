package playerdemo;

import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.graphics.Image;
import jangl.graphics.Texture;
import jangl.io.keyboard.Keyboard;
import jangl.shapes.Rect;
import jangl.time.Clock;
import org.lwjgl.glfw.GLFW;

public class Player implements AutoCloseable {
    private final float speedX;
    private final float speedY;
    private final Image image;

    public Player(ScreenCoords coords, float speed) {
        this.speedX = PixelCoords.distXtoScreenDist(speed);
        this.speedY = PixelCoords.distYtoScreenDist(speed);

        this.image = new Image(
                new Rect(coords, PixelCoords.distXtoScreenDist(50), PixelCoords.distYtoScreenDist(50)),
                new Texture("src/demo/demoResources/playerDemo/player.png")
        );
    }

    public void update() {
        float xDist = (float) (this.speedX * Clock.getTimeDelta());
        float yDist = (float) (this.speedY * Clock.getTimeDelta());
        Rect rect = this.image.getRect();

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_W)) {
            rect.shift(0, yDist);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_S)) {
            rect.shift(0, -yDist);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_D)) {
            rect.shift(xDist, 0);
        }

        if (Keyboard.getKeyDown(GLFW.GLFW_KEY_A)) {
            rect.shift(-xDist, 0);
        }
    }

    public void draw() {
        this.image.draw();
    }

    @Override
    public void close() {
        this.image.close();
    }
}
