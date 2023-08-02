package jangl.graphics.postprocessing;

import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.VertexShader;
import jangl.graphics.shaders.premade.TextureShaderFrag;
import jangl.graphics.shaders.premade.TextureShaderVert;

import java.util.ArrayList;
import java.util.List;

public class PostProcessing {
    private static boolean initialized = false;
    private static final List<PipelineItem> pipeline = new ArrayList<>();

    private PostProcessing() {
        
    }
    
    public static void init() {
        if (initialized) {
            return;
        }

        VertexShader vertShader = new TextureShaderVert();
        vertShader.setObeyCamera(false);

        addToPipeline(
                new PipelineItem(
                        new ShaderProgram(vertShader, new TextureShaderFrag())
                )
        );

        initialized = true;
    }

    /**
     * Adds a new pipeline item to the end of the pipeline.
     * @param item The pipeline item to add.
     */
    public static void addToPipeline(PipelineItem item) {
        pipeline.add(item);
    }

    /**
     * @return A shallow copy of the pipeline. You can (and are encouraged to) modify this if needed. Just note that
     *         there *must* be at least one item in the pipeline.
     */
    public static List<PipelineItem> getPipeline() {
        return pipeline;
    }

    /**
     * Run this before rendering the first frame of the pipeline. This should be done automatically within JANGL.update()
     */
    public static void start() {
        pipeline.get(0).bind();
    }

    /**
     * Run this at the end of the frame. This should be done automatically within JANGL.update().
     */
    public static void end() {
        for (int i = 1; i < pipeline.size(); i++) {
            PipelineItem item = pipeline.get(i);
            item.bind();
            pipeline.get(i - 1).draw();
        }

        pipeline.get(0).unbind();  // it doesn't matter which one to call to unbind
        pipeline.get(pipeline.size() - 1).draw();
    }
}
