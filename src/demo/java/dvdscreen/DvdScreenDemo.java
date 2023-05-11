package dvdscreen;

import jglt.JANGL;
import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.io.Window;
import jglt.shapes.Circle;
import jglt.time.Clock;

public class DvdScreenDemo {
    private final Circle circle;
    private int xDir;
    private int yDir;
    private final float speedX;
    private final float speedY;

    public DvdScreenDemo() {
        JANGL.init(1600, 900);

        this.circle = new Circle(new ScreenCoords(0.4f, 1f),  0.1f, 100);
        this.xDir = 1;
        this.yDir = 1;
        this.speedX = PixelCoords.distXToScreenDist(5);
        this.speedY = PixelCoords.distYToScreenDist(5);
    }

    public void update() {
        this.circle.shift(this.xDir * this.speedX, this.yDir * this.speedY);

        ScreenCoords circleCenter = this.circle.getCenter();

        if (circleCenter.x > 1 || circleCenter.x < -1) {
            this.xDir *= -1;
        }

        if (circleCenter.y > 1 || circleCenter.y < -1) {
            this.yDir *= -1;
        }
    }

    public void draw() {
        Window.clear();
        this.circle.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            JANGL.update();

            this.draw();
            this.update();

            // Tick the clock so the FPS is equal to 60
            Clock.busyTick(60);
        }

        Window.close();
    }

    public static void main(String[] args) {
        new DvdScreenDemo().run();
    }
}
