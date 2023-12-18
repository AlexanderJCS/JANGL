package guides.fontguide;

import jangl.Jangl;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Font;
import jangl.graphics.font.Text;
import jangl.graphics.font.TextBuilder;
import jangl.io.Window;

public class FontGuide {
    private final Text text;

    public FontGuide() {
        Font myFont = new Font(
                "src/test/resources/guide/fontGuide/arial.fnt",
                "src/test/resources/guide/fontGuide/arial.png"
                );

        this.text = new TextBuilder(myFont, "Hello World!")
                .setCoords(new WorldCoords(0.1f, 0.9f))
                .setYHeight(0.1f)
                .toText();
    }

    public void run() {
        while (Window.shouldRun()) {
            Window.clear();
            this.text.draw();

            Jangl.update();
        }

        // It is important to close the Font object in addition to the text object
        // to avoid a memory leak. To do so, we can call the close() method.
        // It is important to note that text.close() does not close the font.
        this.text.getFont().close();
        this.text.close();
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);  // screen width in pixels, screen height in pixels
        Window.setVsync(true);

        new FontGuide().run();  // run a new FontGuide

        Window.close();
    }
}