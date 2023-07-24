package shapedemo;

import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
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
        this.rect = new Rect(new WorldCoords(0.8f, 0.6f), 0.25f, 0.25f);
        this.circle = new Circle(new WorldCoords(1.3f, 0.5f), 0.1f, 70);
        this.triangle = new Triangle(new WorldCoords(0.5f, 0.3f), new WorldCoords(0.8f, 0.3f), new WorldCoords(0.5f, 0.6f));
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

        this.rect.setWidth(this.rect.getWidth() + (float) (Clock.getTimeDelta() * 0.2));

        this.triangle.getTransform().rotate((float) Clock.getTimeDelta());
        this.rect.getTransform().setPos(Mouse.getMousePos().x, Mouse.getMousePos().y);

        // Draw a green background if the rectangle collides with the circle
        // Otherwise draw a red background
        if (Shape.collides(this.rect, this.circle) || Shape.collides(this.rect, this.triangle)) {
            Window.setClearColor(ColorFactory.fromNormalized(0, 0.8f, 0, 1));
        } else {
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
        this.triangle.close();
    }

    public static void main(String[] args) {
        // Initialize JANGL with a screen width of 1600 pixels and a screen height of 900 pixels
        JANGL.init(1600, 900);
        Window.setVsync(true);

        new ShapeDemo().run();
    }
}
