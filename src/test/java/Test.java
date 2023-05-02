import jglt.coords.ScreenCoords;
import jglt.io.Keyboard;
import jglt.io.Mouse;
import jglt.io.Window;
import jglt.shapes.Circle;
import jglt.shapes.Rect;
import jglt.time.Clock;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class Test {
    public Test() {
        Mouse.init();
        Keyboard.init();
        Window.init(1600, 900);
    }

    public void run() {
        // Create a rect and a circle using a try with resources statement.
        // If a shape is not closed, (either manually or with the try with resources) a memory leak will occur.
        try (
                Rect rect = new Rect(new ScreenCoords(-0.25f, -0.5f), 0.5f, 0.5f);
                Circle circle = new Circle(new ScreenCoords(0, 0.5f), 0.25f, 100)
        ) {
            // While the "X" button on the top right of the window is not pressed
            while (!glfwWindowShouldClose(Window.getWindow())) {
                glfwPollEvents();
                glfwSwapBuffers(Window.getWindow());

                // Make the screen black for the next frame
                glClear(GL_COLOR_BUFFER_BIT);

                // Draw the rectangle to the screen
                rect.drawModel();
                circle.draw();

                // Tick the clock so the FPS is equal to 60
                Clock.busyTick(60);
            }
        }

        // Terminate GLFW when the process is done
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Test().run();
    }
}
