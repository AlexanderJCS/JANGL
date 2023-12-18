package demos.fontdemo;

import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.graphics.font.TextBuilder;
import jangl.io.Window;
import jangl.time.Clock;

public class FontDemo {
    private final Text rightJustify;
    private final Text leftJustify;
    private final Text centerJustify;
    private float hue;

    public FontDemo() {
        Font arial = new Font(
                "src/test/resources/demo/font/arial.fnt",
                "src/test/resources/demo/font/arial.png"
        );

        this.leftJustify = new TextBuilder(arial, "abcdefghijklmnopqrstuvwxyz\n0123456789\t<- tab")
                .setCoords(new WorldCoords(0.3f, 0.7f))
                .setYHeight(0.05f)
                .toText();

        this.rightJustify = new TextBuilder(arial, "Right Justification!")
                .setCoords(new WorldCoords(WorldCoords.getTopRight().x - 0.3f, 0.5f))
                .setYHeight(0.05f)
                .setJustification(Justify.RIGHT)
                .toText();

        this.centerJustify = new TextBuilder(arial, "Center Justification!")
                .setCoords(new WorldCoords(WorldCoords.getMiddle().x, 0.3f))
                .setYHeight(0.05f)
                .setJustification(Justify.CENTER)
                .toText();

        this.hue = 0;
    }

    public void draw() {
        Window.clear();

        this.leftJustify.draw();
        this.rightJustify.draw();
        this.centerJustify.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            this.hue += Clock.getTimeDeltaf() * 0.2f;
            this.leftJustify.getFont().setFontColor(ColorFactory.fromNormHSVA(this.hue, 1, 1, 1));

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
