package dinosaurDemo;

import jangl.JANGL;
import jangl.coords.ScreenCoords;
import jangl.graphics.Image;
import jangl.graphics.Texture;
import jangl.graphics.shaders.ColorShader;
import jangl.io.Window;
import jangl.shapes.Rect;
import jangl.time.Clock;

public class RunnerDemo {
    private final Player player;
    private final Image ground;
    private final CactusSpawner cactusSpawner;
    private final Rect background;
    private final ColorShader backgroundColor;

    public RunnerDemo() {
        JANGL.init(1600, 900);

        this.background = new Rect(new ScreenCoords(-1, 1), 2, 2);
        this.backgroundColor = new ColorShader(30 / 256f, 170 / 256f, 200 / 256f, 1);

        this.ground = new Image(
                new Rect(new ScreenCoords(-1f, -0.5f), 2f, 0.5f),
                new Texture("src/demo/demoResources/dinosaurDemo/sand.png")
        );

        this.player = new Player(0.2f, 0.05f, this.ground.getRect().y1);
        this.cactusSpawner = new CactusSpawner(2, this.ground.getRect().y1);
    }

    public void draw() {
        Window.clear();

        this.background.draw(this.backgroundColor);

        this.player.draw();
        this.ground.draw();

        this.cactusSpawner.draw();
    }

    public void update() {
        JANGL.update();

        this.player.update();
        this.cactusSpawner.update(this.player);
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();
            this.update();

            Clock.busyTick(60);
        }

        Window.close();
    }

    public static void main(String[] args) {
        new RunnerDemo().run();
    }
}
