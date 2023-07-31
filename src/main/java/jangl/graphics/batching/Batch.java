package jangl.graphics.batching;

import jangl.graphics.Bindable;
import jangl.graphics.models.TexturedModel;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.VertexShader;
import jangl.graphics.shaders.premade.DefaultVertShader;
import jangl.shapes.Transform;

public class Batch implements AutoCloseable {
    private static final ShaderProgram defaultShader = new ShaderProgram(new DefaultVertShader());
    private final TexturedModel model;
    private final Transform transform;

    public Batch(BatchBuilder builder) {
        this.model = new TexturedModel(
                builder.getVerticesLocal(),
                builder.getIndices(),
                builder.getTexCoords()
        );

        this.transform = new Transform();
        this.transform.shift(builder.getMiddle());
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
                this.transform.getMatrix()
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

    public Transform getTransform() {
        return this.transform;
    }

    @Override
    public void close() {
        this.model.close();
    }
}
