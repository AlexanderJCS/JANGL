package jangl.graphics.batching;

import jangl.graphics.Bindable;
import jangl.graphics.models.TexturedModel;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.VertexShader;
import jangl.graphics.shaders.premade.DefaultVertShader;
import org.joml.Matrix4f;

public class Batch implements AutoCloseable {
    private static final ShaderProgram defaultShader = new ShaderProgram(new DefaultVertShader());
    private final TexturedModel model;

    public Batch(BatchBuilder builder) {
        this.model = new TexturedModel(
                builder.getVertices(),
                builder.getIndices(),
                builder.getTexCoords()
        );
    }

    public void draw() {
        ShaderProgram boundProgram = ShaderProgram.getBoundProgram();

        boolean usingDefaultShader = false;

        if (boundProgram == null) {
            usingDefaultShader = true;
            boundProgram = defaultShader;
            defaultShader.bind();
        }

        VertexShader vertexShader = boundProgram.getVertexShader();

        vertexShader.setMatrixUniforms(
                boundProgram.getProgramID(),
                new Matrix4f().identity()
        );

        this.model.render();

        if (usingDefaultShader) {
            defaultShader.unbind();
        }
    }

    public void draw(Bindable bindable) {
        bindable.bind();
        this.draw();
        bindable.unbind();
    }

    @Override
    public void close() {
        this.model.close();
    }
}
