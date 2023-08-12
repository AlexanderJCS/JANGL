package demos.postprocessingdemo;

import jangl.graphics.shaders.FragmentShader;

import java.io.UncheckedIOException;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;

public class InvertColorsFrag extends FragmentShader {
    public InvertColorsFrag() throws UncheckedIOException {
        super("src/demo/java/demos.postprocessingdemo/invertColors.frag");
    }

    @Override
    public void setUniforms(int programID) {
        int location = glGetUniformLocation(programID, "texSampler");
        glUniform1i(location, 0);
    }
}
