package shaderdemo;

import jangl.coords.NDCoords;
import jangl.graphics.shaders.VertexShader;
import org.lwjgl.opengl.GL46;

import java.io.UncheckedIOException;

public class TranslationShader extends VertexShader {
    private final NDCoords offset;

    public TranslationShader(NDCoords offset) throws UncheckedIOException {
        super("src/demo/java/shaderdemo/transformShader.vert");

        this.offset = offset;
    }

    @Override
    public void setUniforms(int programID) {
        int location = GL46.glGetUniformLocation(programID, "offset");
        GL46.glUniform2f(location, this.offset.x, this.offset.y);
    }
}
