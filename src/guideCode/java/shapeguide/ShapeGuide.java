package shapeguide;

import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Triangle;
import jangl.time.Clock;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeGuide() {
        this.rect = new Rect(new NDCoords(-0.75f, 0.75f), 0.6f, 0.6f);
        this.circle = new Circle(new NDCoords(-0.5f, -0.5f), 0.25f, 48);
        this.triangle = new Triangle(
                new NDCoords(0, -0.3f),
                new NDCoords(0.5f, -0.3f),
                new NDCoords(0.25f, 0.3f)
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
            try {
                Clock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        new ShapeGuide().run();
        Window.close();
    }
}
