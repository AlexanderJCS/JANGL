package jangl.graphics.postprocessing;

import jangl.coords.WorldCoords;
import jangl.io.Window;
import jangl.shapes.Rect;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL46.*;

public class PostProcessing {
    private final int framebuffer;
    private final int renderBuffer;
    private final int framebufferTexture;
    private final Rect rect;

    public PostProcessing() {
        this.framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, this.framebuffer);

        this.framebufferTexture = genFramebufferTexture();
        this.renderBuffer = genRenderBuffer();

        this.errorCheck();

        // For some reason the top left needs to be at 0, 0 (the bottom left of the screen) and
        // the height needs to be negative. No idea why, but if I don't do this the image appears flipped.
        this.rect = new Rect(
                new WorldCoords(0, 0), WorldCoords.getTopRight().x, -WorldCoords.getTopRight().y
        );
    }

    private int genFrameBuffer() {
        return glGenFramebuffers();
    }

    private int genFramebufferTexture() {
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

    private int genRenderBuffer() {
        int rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, Window.getScreenWidth(), Window.getScreenHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

        return rbo;
    }

    private void errorCheck() {
        int fboStatus = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (fboStatus != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println(
                    "FBO status has an error. Error code: " + fboStatus
            );
        }
    }

    public int getFramebufferID() {
        return this.framebuffer;
    }

    public int getFramebufferTextureID() {
        return this.framebufferTexture;
    }

    public void draw() {
        this.rect.draw();
    }
}
