package playerdemo;

import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.Image;
import jglt.graphics.texture.Texture;
import jglt.io.Keyboard;
import jglt.shapes.Rect;
import jglt.time.Clock;
import org.lwjgl.glfw.GLFW;

public class Player implements AutoCloseable {
    private float speedX;
    private float speedY;
    private Image image;

    public Player(ScreenCoords coords, float speed) {
        this.speedX = PixelCoords.distXToScreenDist(speed);
        this.speedY = PixelCoords.distYToScreenDist(speed);

        this.image = new Image(
                new Rect(coords, PixelCoords.distXToScreenDist(50), PixelCoords.distYToScreenDist(50)),
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
