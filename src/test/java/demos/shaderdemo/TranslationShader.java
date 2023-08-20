package demos.shaderdemo;

import jangl.coords.WorldCoords;
import jangl.graphics.shaders.VertexShader;
import org.lwjgl.opengl.GL41;

import java.io.UncheckedIOException;

public class TranslationShader extends VertexShader {
    private final WorldCoords offset;

    public TranslationShader(WorldCoords offset) throws UncheckedIOException {
        super("src/test/java/demos/shaderdemo/transformShader.vert");

        this.offset = offset;
    }

    @Override
    public void setUniforms(int programID) {
        int location = GL41.glGetUniformLocation(programID, "offset");
        GL41.glUniform2f(location, this.offset.x, this.offset.y);
    }
}
