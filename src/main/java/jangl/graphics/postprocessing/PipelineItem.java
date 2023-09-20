package jangl.graphics.postprocessing;

import jangl.coords.WorldCoords;
import jangl.graphics.Bindable;
import jangl.graphics.shaders.ShaderProgram;
import jangl.io.Window;
import jangl.shapes.Rect;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL41.*;

/**
 * WARNING: Make sure to close the shader program using this.getShaderProgram().close() before deleting this object.
 */
public class PipelineItem implements Bindable, AutoCloseable {
    // For some reason the top left needs to be at 0, 0 (the bottom left of the screen) and
    // the height needs to be negative. No idea why, but if I don't do this the image appears flipped.
    private static final Rect SCREEN_RECT = new Rect(
            new WorldCoords(0, 0),
            WorldCoords.getTopRight().x,
            -WorldCoords.getTopRight().y
    );

    private final int framebuffer;
    private final int framebufferTexture;
    private final ShaderProgram shaderProgram;

    /**
     * Creates a pipeline item to be used in post-processing.
     * @param shaderProgram The shader program to be used for post-processing. WARNING: to avoid issues, the vertex
     *                      shader should not obey the camera.
     */
    public PipelineItem(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;

        this.framebuffer = genFramebuffer();
        this.bind();
        this.framebufferTexture = genFramebufferTexture();

        errorCheck();

        this.unbind();
    }

    private static int genFramebuffer() {
        return glGenFramebuffers();
    }

    private static int genFramebufferTexture() {
        int framebufferTexture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, framebufferTexture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, Window.getScreenWidth(), Window.getScreenHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, framebufferTexture, 0);

        return framebufferTexture;
    }

    public int getFramebufferID() {
        return this.framebuffer;
    }

    public int getFramebufferTextureID() {
        return this.framebufferTexture;
    }

    private static void errorCheck() {
        int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (fboStatus != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FBO status has an error. Error code: " + fboStatus);
        }
    }

    public ShaderProgram getShaderProgram() {
        return this.shaderProgram;
    }

    @Override
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.framebuffer);
    }

    @Override
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void draw() {
        glBindTexture(GL_TEXTURE_2D, getFramebufferTextureID());
        shaderProgram.bind();
        SCREEN_RECT.draw();
        shaderProgram.unbind();
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Warning: this doesn't close the shader program associated with the pipeline item since it is not created
     *          by the PipelineItem itself. Make sure to do this.getShaderProgram().close() before running this
     */
    @Override
    public void close() {
        glDeleteFramebuffers(this.framebuffer);
        glDeleteTextures(this.framebufferTexture);
    }
}
