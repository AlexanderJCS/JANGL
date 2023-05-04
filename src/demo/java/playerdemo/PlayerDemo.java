package playerdemo;

import jglt.io.Window;
import jglt.time.Clock;
import jglt.JGLT;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.glClear;

public class PlayerDemo {
    private final Player player;

    public PlayerDemo() {
        this.player = new Player(400);
    }

    private void draw() {
        this.player.draw();
    }

    private void update() {
        this.player.update();
    }

    public void run() {
        // While the "X" button on the top right of the window is not pressed
        while (!glfwWindowShouldClose(Window.getWindow())) {
            JGLT.update();

            // Make the screen black for the next frame
            glClear(GL_COLOR_BUFFER_BIT);

            this.update();
            this.draw();

            // Tick the clock so the FPS is equal to 60
            Clock.busyTick(60);
        }

        // Terminate GLFW when the process is done
        glfwTerminate();
    }

    public static void main(String[] args) {
        JGLT.init(1600, 900);
        new PlayerDemo().run();
    }
}
