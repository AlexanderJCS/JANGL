package demos.icondemo;

import jangl.JANGL;
import jangl.graphics.font.Font;
import jangl.graphics.font.Justify;
import jangl.graphics.font.Text;
import jangl.graphics.font.TextBuilder;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;

/**
 * This demo tests the customization of the mouse cursor and the window desktop icon.
 * The desktop icon does not set properly on macOS because GLFW does not support it.
 */
public class IconDemo implements AutoCloseable {
    TextureBuilder texture;
    private final Text text;

    public IconDemo() {
        this.texture = new TextureBuilder().setImagePath("src/test/resources/demo/iconDemo/circle.png");

        Font arial = new Font(
                "src/test/resources/demo/font/arial.fnt",
                "src/test/resources/demo/font/arial.png"
        );

        this.text = new TextBuilder(arial,"Notice how the cursor and the window icon changed.\nThe window icon does not change on macOS.")
                .setYHeight(0.05f)
                .setJustification(Justify.CENTER)
                .toText();

        Window.setIcon(this.texture);
        Window.setCursor(this.texture);
    }

    public void run() {
        while (Window.shouldRun()) {
            Window.clear();

            this.text.draw();

            JANGL.update();
        }
    }

    @Override
    public void close() {
        this.text.close();
    }

    public static void main(String[] args) {
        JANGL.init(0.75f, 16f/9);
        Window.setVsync(true);

        IconDemo iconDemo = new IconDemo();
        iconDemo.run();
        iconDemo.close();

        Window.close();
    }
}
