package fontdemo;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.parser.Font;
import jangl.io.Window;
import jangl.time.GameClock;

public class FontDemo {
    private final Text text;

    public FontDemo() {
        Font arial = new Font(
                "src/demo/demoResources/font/arial.fnt",
                "src/demo/demoResources/font/arial.png"
        );

        this.text = new Text(
                arial, "abcdefghijklmnopqrstuvwxyz", new ScreenCoords(-0.7f, 0), 0.1f
        );
    }

    public void draw() {
        Window.clear();
        this.text.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();
            JANGL.update();

            // Run the window at 60 FPS, handling any interrupted exceptions that may occur
            try {
                GameClock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);
        new FontDemo().run();
    }
}
