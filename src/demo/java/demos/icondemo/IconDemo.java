package demos.icondemo;

import jangl.JANGL;
import jangl.graphics.textures.TextureBuilder;
import jangl.io.Window;

/**
 * This demo tests the customization of the mouse cursor and the window desktop icon.
 * The desktop icon does not set properly on macOS because GLFW does not support it.
 */
public class IconDemo {
    TextureBuilder texture;

    public IconDemo() {
        this.texture = new TextureBuilder().setImagePath("src/demo/demoResources/iconDemo/circle.png");

        Window.setIcon(this.texture);
        Window.setCursor(this.texture);
    }

    public void run() {
        while (Window.shouldRun()) {
            Window.clear();
            JANGL.update();
        }
    }

    public static void main(String[] args) {
        JANGL.init(0.75f, 16f/9);
        Window.setVsync(true);

        new IconDemo().run();

        Window.close();
    }
}
