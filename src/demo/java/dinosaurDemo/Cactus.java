package dinosaurDemo;

import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.graphics.Image;
import jangl.graphics.Texture;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class Cactus extends Image {
    private final float speed;

    public Cactus(float floorHeight, float speed) {
        super(
                new Rect(new ScreenCoords(1, floorHeight + PixelCoords.distYToScreenDist(100)),
                        PixelCoords.distXToScreenDist(50),
                        PixelCoords.distYToScreenDist(100)
                ),
                new Texture("src/demo/demoResources/dinosaurDemo/cactus.png")
        );

        this.speed = speed;
    }

    public void update() {
        this.getRect().shift(-this.speed * (float) Clock.getTimeDelta(), 0);
    }
}
