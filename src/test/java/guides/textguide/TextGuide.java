package guides.textguide;

import jangl.Jangl;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.graphics.font.TextBuilder;
import jangl.io.Window;

public class TextGuide {
    private final Text text;

    public TextGuide() {
        Font myFont = new Font(
                "src/test/resources/guide/textGuide/arial.fnt",
                "src/test/resources/guide/textGuide/arial.png"
        );

        myFont.setFontColor(ColorFactory.RED);  // sets the color of the font to red

        this.text = new TextBuilder(myFont, "Hello World", WorldCoords.getMiddle())
                .setJustification(Justify.CENTER)  // sets the justification of the text
                .setYHeight(0.1f)  // sets the height of the text in world coords, essentially the font size
                .setWrapWidth(0.4f)  // sets the width at which the text will wrap (this text cannot be more than 0.4 world coords wide)
                .setYCutoff(0.2f)  // sets the height at which the text will be cut off (this text cannot be more than 0.2 world coords tall)
                .toText();  // builds the text object
    }

    public void run() {
        while (Window.shouldRun()) {
            Window.clear();
            this.text.draw();

            Jangl.update();
        }
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);  // screen width in pixels, screen height in pixels
        Window.setVsync(true);

        TextGuide textGuide = new TextGuide();
        textGuide.run();

        Window.close();
    }
}