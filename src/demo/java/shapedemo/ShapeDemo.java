package shapedemo;

import jglt.JGLT;
import jglt.coords.ScreenCoords;
import jglt.io.Window;
import jglt.io.events.Event;
import jglt.io.events.Events;
import jglt.shapes.Circle;
import jglt.shapes.Rect;
import jglt.time.Clock;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class ShapeDemo {
    public ShapeDemo() {
        JGLT.init(1600, 900);
    }

    public void run() {
        // Create a rect and a circle using a try with resources statement.
        // If a shape is not closed, (either manually or with the try with resources) a memory leak will occur.
        try (
                Rect rect = new Rect(new ScreenCoords(-0.25f, -0.25f), 0.5f, 0.5f);
                Circle circle = new Circle(new ScreenCoords(0, 0.5f), 0.1f, 4)
        ) {
            // While the "X" button on the top right of the window is not pressed
            while (Window.shouldRun()) {
                JGLT.update();

                // Make the screen black for the next frame
                Window.clear();

                // Draw the rectangle to the screen
                rect.draw();
                circle.draw();

                // Rotate the rectangle and circle 0.01 radians across the center of the screen every second
                rect.rotateAxis(0.1 * Clock.getTimeDelta());

                for (Event event : Events.getEvents()) {
                    System.out.println(event);
                }

                // Tick the clock so the FPS is equal to 60
                Clock.busyTick(60);
            }
        }

        // Terminate GLFW when the process is done
        glfwTerminate();
    }

    public static void main(String[] args) {
        new ShapeDemo().run();
    }
}
