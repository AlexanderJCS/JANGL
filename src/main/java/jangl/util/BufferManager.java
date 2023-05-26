package jangl.util;

import org.lwjgl.BufferUtils;

import java.nio.*;

/**
 * Provides utilities to create buffers easier
 */
public class BufferManager {
    public static FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    public static IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }
}
