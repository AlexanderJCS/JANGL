package shapedemo;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.graphics.shaders.ColorShader;
import jangl.io.Event;
import jangl.io.Window;
import jangl.io.keyboard.Keyboard;
import jangl.io.mouse.Mouse;
import jangl.shapes.Circle;
import jangl.shapes.Rect;
import jangl.shapes.Shape;
import jangl.time.Clock;

public class ShapeDemo {
    public ShapeDemo() {
        JANGL.init(1600, 900);
    }

    public void run() {
        // Create a rect and a circle using a try with resources statement.
        // If a shape is not closed, (either manually or with the try with resources) a memory leak will occur.
        try (
                Rect rect = new Rect(new ScreenCoords(-0.25f, 0.25f), 0.5f, 0.5f);
                Circle circle = new Circle(new ScreenCoords(0, 0.5f), 0.1f, 70);
                ColorShader colorShader = new ColorShader(1, 0, 0, 1);
                Rect background = new Rect(new ScreenCoords(-1, 1), 2, 2)
        ) {
            // While the "X" button on the top right of the window is not pressed
            while (Window.shouldRun()) {
                // Draw a green background if the rectangle collides with the circle
                // Otherwise draw a red background
                if (Shape.collides(rect, circle)) {
                    // Set the color to 0 red, 1, green, 0 blue, 1 alpha (0 transparency)
                    colorShader.setRGBA(0, 0.8f, 0, 1);
                } else {
                    // Set the color to 1 red, 0, green, 0 blue, 1 alpha (0 transparency)
                    colorShader.setRGBA(0.8f, 0, 0, 1);
                }

                // Make the screen black for the next frame
                Window.clear();

                // Draw the background using the given color shader
                background.draw(colorShader);

                // Draw the rectangle and circle
                // These draw calls need to be after the background so the background doesn't overlap the shapes
                rect.draw();
                circle.draw();

                // End your draw method with JANGL.update()
                JANGL.update();

                // Rotate the rectangle and circle 0.01 radians across the center of the screen every second
                rect.rotateAxis(0.25 * Clock.getTimeDelta());
                circle.rotateAxis(0.05 * Clock.getTimeDelta());

                // Print all keyboard and mouse events to the console
                for (Event event : Keyboard.getEvents()) {
                    System.out.println(event);
                }

                for (Event event : Mouse.getEvents()) {
                    System.out.println(event);
                }

                // Tick the clock so the FPS is equal to 60
                Clock.busyTick(60);
                Window.setTitle("JANGL | FPS: " + Math.round(Clock.getSmoothedFps() * 10) / 10f);
            }
        }

        // Close the window when the program is done running
        Window.close();
    }

    public static void main(String[] args) {
        new ShapeDemo().run();
    }
}
