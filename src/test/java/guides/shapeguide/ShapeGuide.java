package guideCode.guides.shapeguide;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Triangle;

public class ShapeGuide {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeGuide() {
        this.rect = new Rect(new WorldCoords(0.25f, 0.75f), 0.6f, 0.3f);
        this.circle = new Circle(new WorldCoords(1.2f, 0.5f), 0.15f, 48);
        this.triangle = new Triangle(
                new WorldCoords(0.1f, 0.1f),
                new WorldCoords(0.4f, 0.1f),
                new WorldCoords(0.1f, 0.4f)
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
        }

        this.rect.close();
        this.circle.close();
        this.triangle.close();
    }

    public static void main(String[] args) {
        // Initialize the window with the width of 1600 pixels and the height of 900 pixels
        JANGL.init(1600, 900);
        Window.setVsync(true);

        new ShapeGuide().run();
        Window.close();
    }
}