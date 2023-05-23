package playerdemo;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.io.Window;
import jangl.time.GameClock;

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
            JANGL.update();

            this.update();
            this.draw();

            // Run the window at 60 FPS, handling any interrupted exceptions that may occur
            try {
                GameClock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Terminate GLFW when the process is done
        Window.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);
        new PlayerDemo().run();
    }
}
