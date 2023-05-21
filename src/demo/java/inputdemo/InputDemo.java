package inputdemo;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.parser.Font;
import jangl.io.Window;
import jangl.io.keyboard.KeyEvent;
import jangl.io.keyboard.Keyboard;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import jangl.time.Clock;
import org.lwjgl.glfw.GLFW;

public class InputDemo implements AutoCloseable {
    private final Font font;
    private final Text text;

    public InputDemo() {
        this.font = new Font("src/demo/demoResources/font/arial.fnt",
                "src/demo/demoResources/font/arial.png");

        this.text = new Text(this.font, "", new ScreenCoords(-0.7f, 0), 0.1f);
        Window.setBackgroundColor(0.7f, 0, 0, 1);
    }

    @Override
    public void close() {
        this.text.close();
        this.font.close();
    }

    private void draw() {
        Window.clear();
        this.text.draw();
    }

    private void updateMouse() {
        for (MouseEvent event : Mouse.getEvents()) {
            if (event.button != 0) {
                continue;
            }

            if (event.action == GLFW.GLFW_PRESS) {
                Window.setBackgroundColor(0, 0.7f, 0, 1);
            } else {
                Window.setBackgroundColor(0.7f, 0, 0, 1);
            }
        }
    }

    private void updateKeyboard() {
        for (KeyEvent event : Keyboard.getEvents()) {
            if (event.action == GLFW.GLFW_RELEASE) {
                continue;
            }

            String textString = this.text.getText();

            // Remove the last letter if backspace is pressed
            if (event.key == GLFW.GLFW_KEY_BACKSPACE && textString.length() > 0) {
                this.text.setText(textString.substring(0, textString.length() - 1));
            } else if (event.key >= ' ' && event.key <= '~') {  // if it is a type-able character
                this.text.setText(textString + event.key);
            }
        }
    }

    private void update() {
        this.updateMouse();
        this.updateKeyboard();
    }

    public void run() {
        while (Window.shouldRun()) {
            JANGL.update();
            this.draw();
            this.update();

            Clock.busyTick(60);
        }

        Window.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);

        try (InputDemo inputDemo = new InputDemo()) {
            inputDemo.run();
        }
    }
}
