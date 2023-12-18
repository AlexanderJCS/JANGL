package demos.shaderdemo;

import jangl.Jangl;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.io.Window;
import jangl.shapes.Rect;

public class ShaderDemo implements AutoCloseable {
    private final ShaderProgram shaderProgram;
    private final Rect rect;

    public ShaderDemo() {
        this.shaderProgram = new ShaderProgram(
                new TranslationShader(new WorldCoords(-0.25f, 0f)),
                new CustomColorShader(ColorFactory.fromNorm(1, 1, 0, 1))
        );

        this.rect = new Rect(new WorldCoords(0.5f, 0.75f), 0.5f, 0.5f);
    }

    public void update() {
        // WARNING: this line of code *must* be run in order to pass the projection matrices to the shader.
        // If this method is not called, whatever you are trying to draw will not appear.
        this.shaderProgram.getVertexShader().setMatrixUniforms(
                this.shaderProgram.getProgramID(),
                this.rect.getTransform().getMatrix()
        );
    }

    public void draw() {
        this.rect.draw(this.shaderProgram);
    }

    public void run() {
        while (Window.shouldRun()) {
            this.update();
            this.draw();

            Jangl.update();
        }
    }

    @Override
    public void close() {
        this.rect.close();
        this.shaderProgram.close();
    }

    public static void main(String[] args) {
        Jangl.init(1600, 900);
        Window.setVsync(true);

        ShaderDemo shaderDemo = new ShaderDemo();
        shaderDemo.run();
        shaderDemo.close();

        Window.close();
    }
}
