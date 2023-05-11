package quickstart;

import jglt.JANGL;
import jglt.coords.ScreenCoords;
import jglt.io.Window;
import jglt.shapes.Rect;

public class Quickstart {
    public Quickstart() {
        // Input the width and height of your screen in pixels.
        JANGL.init(1600, 900);
    }

    public void run() {
        try (Rect rect = new Rect(new ScreenCoords(0, 0), 0.5f, 0.5f)) {
            while (Window.shouldRun()) {
                JANGL.update();
                Window.clear();

                rect.draw();
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new Quickstart().run();
    }
}
