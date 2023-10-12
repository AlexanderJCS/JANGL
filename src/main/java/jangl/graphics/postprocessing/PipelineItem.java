package jangl.graphics.postprocessing;

import jangl.coords.WorldCoords;
import jangl.graphics.Bindable;
import jangl.graphics.shaders.ShaderProgram;
import jangl.io.Window;
import jangl.shapes.Rect;
import org.lwjgl.BufferUtils;

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

    public String convertToString(int targetWidth) {
        byte[] framebufferData = this.read();  // the length of this arr should be a multiple of 4 since it's RGBA data
        byte[] grayscaleData = this.convertRGBAtoGrayscale(framebufferData);

        return grayscaleDataToString(grayscaleData, targetWidth);
    }

    private String grayscaleDataToString(byte[] grayscaleData, int targetWidth) {
        float aspectRatio = (float) Window.getScreenHeight() / Window.getScreenWidth();
        int targetHeight = (int) (aspectRatio * targetWidth);

        int x = 0;
        // I have no idea why I need to multiply by 2 here, but I do
        int xJump = Window.getScreenWidth() / targetWidth * 2;

        int y = 0;
        int yJump = Window.getScreenHeight() / targetHeight * 2;

        String darkestToLightest = ".'`^\",:;Il!i><~+_-?][}{1)(|/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
        StringBuilder consoleOutput = new StringBuilder();

        while (y < Window.getScreenHeight()) {
            byte pixelAtCoord = grayscaleData[y * Window.getScreenWidth() + x];

            // brightness value ranging from 0-1
            float brightness = pixelAtCoord / 127f;

            // Prevent off-by-one index out of bounds errors by clamping the max to 67
            int darkestToLightestIndex = Math.min(67, Math.round(brightness * darkestToLightest.length()));
            char brightnessChar = darkestToLightest.charAt(darkestToLightestIndex);

            consoleOutput.append(brightnessChar);
            // repeat the line to double the char, since chars are 1/2 width than height
            consoleOutput.append(brightnessChar);

            x += xJump;
            if (x > Window.getScreenWidth()) {
                x = 0;
                y += yJump;

                consoleOutput.append("\n");
            }
        }

        return consoleOutput.toString();
    }

    private byte[] convertRGBAtoGrayscale(byte[] rgbaData) {
        byte[] grayscaleData = new byte[rgbaData.length / 4];

        for (int i = 0; i < grayscaleData.length; i++) {
            // average the RGBA data to
            float average = (
                    rgbaData[i * 4]        // red
                            + rgbaData[i * 4 + 1]  // green
                            + rgbaData[i * 4 + 2]  // blue

            ) / 3f;

            // Multiply by alpha and convert the average to type byte.
            byte byteAverage = (byte) (average * ((int) (rgbaData[i * 4 + 3]) + 128) / 255);

            grayscaleData[i] = byteAverage;
        }

        return grayscaleData;
    }

    /**
     * @return The byte data, in RGBA format, of the framebuffer. The first byte is the R channel, the next byte is the G channel, etc.
     */
    public byte[] read() {
        glBindBuffer(GL_READ_FRAMEBUFFER, this.getFramebufferID());

        // Create a ByteBuffer that the framebuffer data will be written to
        int bufferSize = Window.getScreenWidth() * Window.getScreenHeight() * 4;
        ByteBuffer framebufferContents = BufferUtils.createByteBuffer(bufferSize);

        glReadPixels(
                0, 0,
                Window.getScreenWidth(), Window.getScreenHeight(),
                GL_RGBA, GL_BYTE,
                framebufferContents
        );

        // Unbind the read framebuffer
        glBindBuffer(GL_READ_FRAMEBUFFER, 0);

        // Transfer the ByteBuffer to a byte array
        byte[] byteArray = new byte[bufferSize];
        framebufferContents.get(byteArray);

        return byteArray;
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
