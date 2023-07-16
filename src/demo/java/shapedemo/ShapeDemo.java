package shapedemo;

import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Shape;
import jangl.shapes.Triangle;
import jangl.time.Clock;

public class ShapeDemo {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeDemo() {
        this.rect = new Rect(new NDCoords(0.25f, 0.25f), 0.25f, 0.25f);
        this.circle = new Circle(new NDCoords(0.75f, 0.5f), 0.1f, 70);
        this.triangle = new Triangle(new NDCoords(0.5f, 0.3f), new NDCoords(0.8f, 0.3f), new NDCoords(0.5f, 0.6f));
    }

    public void draw() {
        // Clear the screen by making it the background color as a blank slate for the next frame
        Window.clear();

        // Draw the rectangle and circle
        // These draw calls need to be after the background so the background doesn't overlap the shapes
        this.rect.draw();
        this.circle.draw();
        this.triangle.draw();
    }

    public void update() {
        // Update JANGL so events can be received and the screen doesn't say "not responding"
        JANGL.update();

        // Rotate the rectangle and circle 0.01 radians across the center of the screen every second
        // this.rect.getTransform().rotate((float) Clock.getTimeDelta());
        this.circle.getTransform().rotate((float) Clock.getTimeDelta());
        this.triangle.getTransform().rotate((float) Clock.getTimeDelta());
        this.rect.getTransform().rotate((float) Clock.getTimeDelta());
        // Draw a green background if the rectangle collides with the circle
        // Otherwise draw a red background
        if (Shape.collides(this.rect, this.circle)) {
            // Set the color to 0 red, 1, green, 0 blue, 1 alpha (0 transparency)
            Window.setClearColor(ColorFactory.fromNormalized(0, 0.8f, 0, 1));
        } else {
            // Set the color to 1 red, 0, green, 0 blue, 1 alpha (0 transparency)
            Window.setClearColor(ColorFactory.fromNormalized(0.8f, 0, 0, 1));
        }
    }

    public void run() {
        // While the "X" button on the top right of the window is not pressed
        while (Window.shouldRun()) {
            this.update();
            this.draw();
        }

        // Close the window when the program is done running
        Window.close();

        // Close all shapes and textures the program used. This is important in order to prevent memory leaks
        this.rect.close();
        this.circle.close();
    }

    public static void main(String[] args) {
        // Initialize JANGL with a screen width of 1600 pixels and a screen height of 900 pixels
        JANGL.init(1600, 900);
        Window.setVsync(true);

        new ShapeDemo().run();
    }
}
