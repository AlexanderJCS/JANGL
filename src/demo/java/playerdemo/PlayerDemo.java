package playerdemo;

import jglt.coords.ScreenCoords;
import jglt.io.Window;
import jglt.time.Clock;
import jglt.JGLT;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class PlayerDemo {
    private final Player player;

    public PlayerDemo() {
        this.player = new Player(new ScreenCoords(0, 0), 400);
    }

    private void draw() {
        Window.clear();
        this.player.draw();
    }

    private void update() {
        this.player.update();
    }

    public void run() {
        // While the "X" button on the top right of the window is not pressed
        while (Window.shouldRun()) {
            JGLT.update();

            this.update();
            this.draw();

            // Tick the clock so the FPS is equal to 60
            Clock.busyTick(60);

            // Set the window title equal to 1 / the time the last frame took
            // which would give FPS
            Window.setTitle("FPS: " + Math.round(1 / Clock.getTimeDelta()));
        }

        // Terminate GLFW when the process is done
        glfwTerminate();
    }

    public static void main(String[] args) {
        JGLT.init(1600, 900);
        new PlayerDemo().run();
    }
}
