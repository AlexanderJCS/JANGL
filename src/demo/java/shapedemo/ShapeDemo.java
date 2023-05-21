package shapedemo;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.graphics.shaders.ColorShader;
import jangl.io.Window;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Shape;
import jangl.time.Clock;

public class ShapeDemo {
    private final Rect rect;
    private final Circle circle;
    private final ColorShader colorShader;
    private final Rect background;

    public ShapeDemo() {
        this.rect = new Rect(new ScreenCoords(-0.25f, 0.25f), 0.5f, 0.5f);
        this.circle = new Circle(new ScreenCoords(0, 0.5f), 0.1f, 70);
        this.colorShader = new ColorShader(1, 0, 0, 1);
        this.background = new Rect(new ScreenCoords(-1, 1), 2, 2);
    }

    public void draw() {
        // Make the screen black for the next frame
        Window.clear();

        // Draw the background using the given color shader
        this.background.draw(this.colorShader);

        // Draw the rectangle and circle
        // These draw calls need to be after the background so the background doesn't overlap the shapes
        this.rect.draw();
        this.circle.draw();
    }

    public void update() {
        // Update JANGL so events can be received and the screen doesn't say "not responding"
        JANGL.update();

        // Rotate the rectangle and circle 0.01 radians across the center of the screen every second
        this.rect.rotateAxis(0.25 * Clock.getTimeDelta());
        this.circle.rotateAxis(0.05 * Clock.getTimeDelta());

        // Draw a green background if the rectangle collides with the circle
        // Otherwise draw a red background
        if (Shape.collides(this.rect, this.circle)) {
            // Set the color to 0 red, 1, green, 0 blue, 1 alpha (0 transparency)
            this.colorShader.setRGBA(0, 0.8f, 0, 1);
        } else {
            // Set the color to 1 red, 0, green, 0 blue, 1 alpha (0 transparency)
            this.colorShader.setRGBA(0.8f, 0, 0, 1);
        }
    }

    public void run() {
        // While the "X" button on the top right of the window is not pressed
        while (Window.shouldRun()) {
            this.update();
            this.draw();

            // Tick the clock so the FPS is equal to 60
            Clock.busyTick(60);
        }

        // Close the window when the program is done running
        Window.close();

        // Close all shapes and textures the program used. This is important in order to prevent memory leaks
        this.rect.close();
        this.circle.close();
        this.colorShader.close();
        this.background.close();
    }

    public static void main(String[] args) {
        // Initialize JANGL with a screen width of 1600 pixels and a screen height of 900 pixels
        JANGL.init(1600, 900);

        new ShapeDemo().run();
    }
}
