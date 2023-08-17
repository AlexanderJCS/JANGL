package demos.fontdemo;

import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.io.Window;
import jangl.time.Clock;

public class FontDemo {
    private final Text rightJustify;
    private final Text leftJustify;
    private float hue;

    public FontDemo() {
        Font arial = new Font(
                "src/demo/demoResources/font/arial.fnt",
                "src/demo/demoResources/font/arial.png"
        );

        this.leftJustify = new Text(
                new WorldCoords(0.3f, 0.7f), arial, 0.05f,
                "abcdefghijklmnopqrstuvwxyz\n0123456789\t<- tab",
                Justify.LEFT  // this argument isn't required since it defaults to left justification
        );

        this.rightJustify = new Text(
                new WorldCoords(WorldCoords.getTopRight().x - 0.3f, 0.5f), arial, 0.05f,
                "Right justification!",
                Justify.RIGHT
        );

        this.hue = 0;
    }

    public void draw() {
        Window.clear();

        this.leftJustify.draw();
        this.rightJustify.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            this.hue += Clock.getTimeDelta() * 0.2f;
            this.leftJustify.getFont().setFontColor(ColorFactory.fromNormalizedHSVA(this.hue, 1, 1, 1));

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
