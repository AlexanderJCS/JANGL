import jglt.coords.ScreenCoords;
import jglt.io.Keyboard;
import jglt.io.Mouse;
import jglt.io.Window;
import jglt.shapes.Rect;
import jglt.time.Clock;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class Test {
    public Test() {
        Mouse.init();
        Keyboard.init();
        Window.init();
    }

    public void run() {
        // Create a rect using a try with resources statement.
        // If a rect is not closed, a memory leak will occur. So it's important to either use a try with resources
        // statement or manually close the rect.
        try (Rect rect = new Rect(new ScreenCoords(-0.5f, -0.5f), 0.5f, 0.5f)) {
            // While the "X" button on the top right of the window is not pressed
            while (!glfwWindowShouldClose(Window.getWindow())) {
                glfwPollEvents();
                glfwSwapBuffers(Window.getWindow());

                // Make the screen black for the next frame
                glClear(GL_COLOR_BUFFER_BIT);

                // Draw the rectangle to the screen
                rect.drawModel();

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
