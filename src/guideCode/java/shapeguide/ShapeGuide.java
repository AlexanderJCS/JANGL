package shapeguide;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Triangle;
import jangl.time.GameClock;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeGuide() {
        this.rect = new Rect(new ScreenCoords(-0.75f, 0.75f), 0.6f, 0.6f);
        this.circle = new Circle(new ScreenCoords(-0.5f, -0.5f), 0.25f, 48);
        this.triangle = new Triangle(
                new ScreenCoords(0, -0.3f),
                new ScreenCoords(0.5f, -0.3f),
                new ScreenCoords(0.25f, 0.3f)
        );
    }

    public void draw() {
        Window.clear();

        this.rect.draw();
        this.circle.draw();
        this.triangle.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            // This is method is required to be called so the window doesn't say "not responding"
            JANGL.update();

            // Run the window at 60 fps
            GameClock.busyTick(60);
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ShapeGuide().run();
        Window.close();
    }
}
