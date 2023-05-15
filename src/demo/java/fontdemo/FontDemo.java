package fontdemo;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.parser.Font;
import jangl.io.Window;
import jangl.time.Clock;

public class FontDemo {
    private final Text text;

    public FontDemo() {
        Font arial = new Font(
                "src/demo/demoResources/font/arial.fnt",
                "src/demo/demoResources/font/arial.png"
        );

        this.text = new Text(
                arial, "abcdefghijklmnopqrstuvwxyz", new ScreenCoords(-1, 0), 0.1f
        );
    }

    public void draw() {
        this.text.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();
            JANGL.update();
            Clock.busyTick(60);
        }

        Window.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);
        new FontDemo().run();
    }
}
