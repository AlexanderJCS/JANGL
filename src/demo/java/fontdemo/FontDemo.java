package fontdemo;

import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.parser.Font;
import jangl.io.Window;

public class FontDemo {
    private final Text text;

    public FontDemo() {
        Font arial = new Font(
                "src/demo/demoResources/font/arial.fnt",
                "src/demo/demoResources/font/arial.png"
        );

        this.text = new Text(
                new NDCoords(-0.7f, 0), arial, 0.1f,
                "abcdefghijklmnopqrstuvwxyz\n0123456789\t<- tab"
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
        }

        Window.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);
        Window.setVsync(true);

        new FontDemo().run();
    }
}
