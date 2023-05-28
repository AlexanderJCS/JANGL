package playerdemo;

import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.io.Window;

public class PlayerDemo {
    private final Player player;

    public PlayerDemo() {
        this.player = new Player(new NDCoords(0, 0), 400);
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
        }

        // Terminate GLFW when the process is done
        Window.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);
        Window.setVsync(true);

        new PlayerDemo().run();
    }
}
