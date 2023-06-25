package quickstart;

import jangl.JANGL;
import jangl.coords.PixelCoords;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class Quickstart implements AutoCloseable {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new NDCoords(0, 0), PixelCoords.distXtoNDC(400), PixelCoords.distYtoNDC(400));
    }

    public void run() {
        while (Window.shouldRun()) {
            JANGL.update();
            Window.clear();

            this.rect.draw();

            // Run the window at 60 FPS, handling any interrupted exceptions that may occur
            try {
                Clock.smartTick(60);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void close() {
        this.rect.close();
    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);

        Quickstart quickstart = new Quickstart();
        quickstart.run();
        quickstart.close();

        Window.close();
    }
}