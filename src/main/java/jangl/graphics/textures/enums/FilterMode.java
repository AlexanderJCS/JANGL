package jangl.graphics.textures.enums;

import static org.lwjgl.opengl.GL41.*;

public enum FilterMode {
    LINEAR, NEAREST;

    public int toInteger() {
        if (this == LINEAR) {
            return GL_LINEAR;
        }

        return GL_NEAREST;
    }
}
