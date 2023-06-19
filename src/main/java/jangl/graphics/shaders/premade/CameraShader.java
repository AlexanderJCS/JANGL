package jangl.graphics.shaders.premade;

import jangl.graphics.shaders.VertexShader;
import java.io.UncheckedIOException;

public class CameraShader extends VertexShader {
    public CameraShader() throws UncheckedIOException {
        super(CameraShader.class.getResourceAsStream("/shaders/cameraShader/cameraShader.vert"));
    }
}
