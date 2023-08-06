package jangl.graphics.shaders.premade;

import jangl.graphics.shaders.VertexShader;

import java.io.UncheckedIOException;

/**
 * A vertex shader that does nothing. That's it.
 */
public class DefaultVertShader extends VertexShader {

    public DefaultVertShader() throws UncheckedIOException {
        super(DefaultVertShader.class.getResourceAsStream("/shaders/defaultVertShader/defaultShader.vert"));
    }
}
