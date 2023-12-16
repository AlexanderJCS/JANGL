package jangl.graphics.textures.enums;

import static org.lwjgl.opengl.GL41.*;

public enum WrapMode {
    /**
     * Repeats the texture.
     */
    REPEAT,
    /**
     * Repeats the texture, but mirrors it each time across the x and y-axis. This creates for a seamless texture.
     */
    MIRRORED_REPEAT,
    /**
     * Clamps the texture to the edge of the model, ensuring that it does not repeat. Best if the texture should not
     * repeat.
     */
    CLAMP_TO_EDGE;

    public int toInteger() {
        if (this == REPEAT) {
            return GL_REPEAT;
        } else if (this == MIRRORED_REPEAT) {
            return GL_MIRRORED_REPEAT;
        }

        return GL_CLAMP_TO_EDGE;
    }
}
