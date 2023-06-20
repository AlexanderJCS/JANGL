package shaderdemo;

import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.coords.NDCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.io.Window;
import jangl.shapes.Rect;

public class ShaderDemo implements AutoCloseable {
    private final ShaderProgram shaderProgram;
    private final Rect rect;

    public ShaderDemo() {
        this.shaderProgram = new ShaderProgram(
                new TranslationShader(new NDCoords(-0.5f, 0.5f)),
                new CustomColorShader(ColorFactory.fromNormalized(1, 1, 0, 1))
        );

        this.rect = new Rect(new NDCoords(0, 0), 1, 1);
    }

    public void draw() {
        this.rect.draw(this.shaderProgram);
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            JANGL.update();
        }
    }

    @Override
    public void close() {
        this.rect.close();
        this.shaderProgram.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);
        Window.setVsync(true);

        ShaderDemo shaderDemo = new ShaderDemo();
        shaderDemo.run();
        shaderDemo.close();

        Window.close();
    }
}
