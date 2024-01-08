package demos.textdemo;

import jangl.Jangl;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.graphics.font.TextBuilder;
import jangl.io.Window;
import jangl.time.Clock;

public class TextDemo {
    private final Text rightJustify;
    private final Text leftJustify;
    private final Text centerJustify;
    private final Text wrapAndCutoff;
    private float hue;

    public TextDemo() {
        Font arial = new Font(
                "src/test/resources/demo/font/arial.fnt",
                "src/test/resources/demo/font/arial.png"
        );

        this.leftJustify = new TextBuilder(arial, "abcdefghijklmnopqrstuvwxyz\n0123456789\t<- tab")
                .setCoords(new WorldCoords(0.3f, 0.7f))
                .setHeight(0.05f)  // 0.05f is the default value, so this isn't technically needed. it's only here to showcase
                .toText();

        this.rightJustify = new TextBuilder(arial, "Right Justification!")
                .setCoords(new WorldCoords(WorldCoords.getTopRight().x - 0.3f, 0.5f))
                .setJustification(Justify.RIGHT)
                .toText();

        this.centerJustify = new TextBuilder(arial, "Center Justification!")
                .setCoords(new WorldCoords(WorldCoords.getMiddle().x, 0.3f))
                .setJustification(Justify.CENTER)
                .toText();

        this.wrapAndCutoff = new TextBuilder(arial, "This text is wrapped and cut off once it exceeds the width and the height values assigned to it")
                .setCoords(new WorldCoords(0.3f, 0.2f))
                .setWrapWidth(WorldCoords.getTopRight().x - 0.7f)
                .setYCutoff(0.1f)
                .toText();

        this.hue = 0;
    }

    public void draw() {
        Window.clear();

        this.leftJustify.draw();
        this.rightJustify.draw();
        this.centerJustify.draw();
        this.wrapAndCutoff.draw();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            this.hue += Clock.getTimeDeltaf() * 0.2f;
            this.leftJustify.getFont().setFontColor(ColorFactory.fromNormHSVA(this.hue, 1, 1, 1));

            Jangl.update();
        }

        Window.close();
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);
        Window.setVsync(true);

        new TextDemo().run();
    }
}
