package dinosaurDemo;

import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.Image;
import jglt.graphics.Texture;
import jglt.shapes.Rect;
import jglt.time.Clock;

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
