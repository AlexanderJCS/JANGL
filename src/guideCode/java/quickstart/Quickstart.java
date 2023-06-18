package quickstart;

import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.coords.NDCoords;
import jangl.coords.PixelCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (
                Rect rect = new Rect(
                    new NDCoords(0, 0),
                    PixelCoords.distXtoNDC(400),
                    PixelCoords.distYtoNDC(400)
                );

                ShaderProgram yellow = new ShaderProgram(new ColorShader(ColorFactory.fromNormalized(1, 1, 0, 1)))
        ) {
            while (Window.shouldRun()) {
                JANGL.update();
                Window.clear();

                rect.draw(yellow);

                // Run the window at 60 FPS, handling any interrupted exceptions that may occur
                try {
                    Clock.smartTick(60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
