package dinosaurDemo;

import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.Image;
import jglt.graphics.Texture;
import jglt.io.keyboard.Keyboard;
import jglt.shapes.Rect;

import jglt.time.Clock;
import org.lwjgl.glfw.GLFW;

public class Player extends Image {
    private float yVel;
    private final float gravity;
    private final float jumpVel;
    private final float groundHeight;

    public Player(float gravity, float jumpVel, float groundHeight) {
        super(
                new Rect(
                        new ScreenCoords(0, groundHeight + PixelCoords.distYToScreenDist(50)),
                        PixelCoords.distXToScreenDist(50), PixelCoords.distYToScreenDist(50)
                ),
                new Texture("src/demo/demoResources/playerDemo/player.png")
        );

        this.yVel = 0;

        this.groundHeight = groundHeight;
        this.jumpVel = jumpVel;
        this.gravity = gravity;
    }

    public void update() {
        if (this.getRect().y2 > this.groundHeight) {
            this.yVel -= this.gravity * Clock.getTimeDelta();
        }

        if (this.getRect().y2 - this.groundHeight <= 0.01f && Keyboard.getKeyDown(GLFW.GLFW_KEY_SPACE)) {
            this.yVel += jumpVel;
        }

        this.getRect().shift(0, yVel);

        if (this.getRect().y2 <= this.groundHeight) {
            this.yVel = 0;
            this.getRect().setPos(new ScreenCoords(-0.5f, this.groundHeight + this.getRect().height));
        }
    }
}
