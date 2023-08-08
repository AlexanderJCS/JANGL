/*
 * This program demonstrates post-processing effects. It is recommended that you have a basic understanding of GLSL shader
 * programing and how post-processing works in order to understand this example code. It is also recommended to see shader demo.
 *
 * When running the program, you should see a black square inside a white background. This is because of the post-processing effect
 * that inverts the colors in the image. Without the post-processing shader, it would look like a white square inside a black background.
 */

package postprocessingdemo;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.graphics.postprocessing.PipelineItem;
import jangl.graphics.postprocessing.PostProcessing;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.VertexShader;
import jangl.graphics.shaders.premade.TextureShaderVert;
import jangl.io.Window;
import jangl.shapes.Rect;

public class PostProcessingDemo implements AutoCloseable {
    private final Rect rect;
    private final PostProcessing postProcessing;

    public PostProcessingDemo() {
        // Create a rect with a width/height of half the screen width/height
        this.rect = new Rect(new WorldCoords(0, 0), WorldCoords.getMiddle().x, WorldCoords.getMiddle().y);

        // Set the rect's center to the middle of the screen
        this.rect.getTransform().setPos(WorldCoords.getMiddle());

        VertexShader vertexShader = new TextureShaderVert();
        vertexShader.setObeyCamera(false);  // it is very important that it does not obey the camera

        this.postProcessing = new PostProcessing();

        this.postProcessing.addToPipeline(new PipelineItem(
                new ShaderProgram(
                        vertexShader,
                        new InvertColorsFrag(),
                        // attribute locations are important in order to work with every GPU
                        TextureShaderVert.getAttribLocations()
                )
            )
        );
    }

    public void draw() {
        Window.clear();

        // Any draw calls within postProcessing.start(), including Window.clear(), will include the post-processing
        // effect with it.
        this.postProcessing.start();
        this.rect.draw();
        this.postProcessing.end();
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

        // Make sure to close all pipeline items that you added manually. The post-processing class will not close them for you.
        for (PipelineItem item : this.postProcessing.getPipeline()) {
            item.getShaderProgram().close();
            item.close();
        }

        this.postProcessing.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);
        Window.setVsync(true);

        PostProcessingDemo postProcessingDemo = new PostProcessingDemo();
        postProcessingDemo.run();
        postProcessingDemo.close();

        Window.close();
    }
}
