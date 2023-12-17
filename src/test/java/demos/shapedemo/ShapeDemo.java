package demos.shapedemo;

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

public class ShapeDemo implements AutoCloseable {
    private final Rect rect;
    private final Circle circle;
    private final Triangle triangle;

    public ShapeDemo() {
        this.rect = new Rect(new WorldCoords(0.8f, 0.6f), 0.25f, 0.25f);
        this.circle = new Circle(new WorldCoords(1.2f, 0.4f), 0.1f, 70);
        this.circle.getTransform().setScale(2f);
        this.triangle = new Triangle(new WorldCoords(0.5f, 0.3f), new WorldCoords(0.8f, 0.3f), new WorldCoords(0.5f, 0.6f));
        this.triangle.getTransform().setScaleX(0.5f);
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

        this.triangle.getTransform().rotate((float) Clock.getTimeDelta());
        this.rect.getTransform().setPos(Mouse.getMousePos().x, Mouse.getMousePos().y);

        // Draw a green background if the rectangle collides with the circle
        // Otherwise draw a red background
        if (Shape.collides(this.rect, this.circle) || Shape.collides(this.rect, this.triangle)) {
            Window.setClearColor(ColorFactory.fromNorm(0, 0.8f, 0, 1));
        } else {
            Window.setClearColor(ColorFactory.fromNorm(0.8f, 0, 0, 1));
        }
    }

    public void run() {
        // While the "X" button on the top right of the window is not pressed
        while (Window.shouldRun()) {
            this.update();
            this.draw();

            Window.setTitle("Shape Demo - FPS: " + Clock.getSmoothedFps());
        }
    }

    @Override
    public void close() {
        this.rect.close();
        this.circle.close();
        this.triangle.close();
    }

    public static void main(String[] args) {
        // Initialize JANGL with a screen width of 1600 pixels and a screen height of 900 pixels
        JANGL.init(1600, 900);
        Window.setVsync(true);

        ShapeDemo shapeDemo = new ShapeDemo();
        shapeDemo.run();
        shapeDemo.close();

        Window.close();
    }
}
