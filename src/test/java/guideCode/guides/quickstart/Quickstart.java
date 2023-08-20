package guides.quickstart;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

public class Quickstart implements AutoCloseable {
    private final Rect rect;

    public Quickstart() {
        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void run() {
        while (Window.shouldRun()) {
            JANGL.update();
            Window.clear();

            this.rect.draw();
        }
    }

    @Override
    public void close() {
        this.rect.close();
    }

    public static void main(String[] args) {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
        Window.setVsync(true);  // turn vsync on

        Quickstart quickstart = new Quickstart();
        quickstart.run();
        quickstart.close();

        Window.close();
    }
}