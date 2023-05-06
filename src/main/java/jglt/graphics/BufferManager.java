package jglt.graphics;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Keeps four buffers for the entire program. This is important since if buffers just keep being created,
 * there will be a memory leak.
 */
public class BufferManager {
    /** Max float buffer capacity */
    private static final int MAX_FB_CAPACITY = 1024;
    /** Max byte buffer capacity */
    private static final int MAX_BB_CAPACITY = 32768;
    /** Max int buffer capacity */
    private static final int MAX_IB_CAPACITY = 1024;

    public static FloatBuffer vboBuffer = BufferUtils.createFloatBuffer(MAX_FB_CAPACITY);
    public static FloatBuffer texCoordsBuffer = BufferUtils.createFloatBuffer(MAX_FB_CAPACITY);
    public static IntBuffer indicesBuffer = BufferUtils.createIntBuffer(MAX_IB_CAPACITY);

    public static DoubleBuffer mouseBuffer1 = BufferUtils.createDoubleBuffer(1);
    public static DoubleBuffer mouseBuffer2 = BufferUtils.createDoubleBuffer(1);
    // This buffer needs to be larger since it's containing image data
    public static ByteBuffer byteBuffer = BufferUtils.createByteBuffer(MAX_BB_CAPACITY);
    public static BufferedImage bufferedImage;

    public static void setFloatBuffer(FloatBuffer fb, float[] newData) throws IllegalArgumentException {
        if (newData.length > MAX_FB_CAPACITY) {
            throw new IllegalArgumentException(
                    "newData is greater than length " + MAX_FB_CAPACITY +
                            ", which will cause a buffer overflow");
        }

        fb.clear();
        fb.limit(newData.length);
        fb.put(newData);
        fb.flip();
    }

    public static void setIntBuffer(IntBuffer ib, int[] newData) {
        if (newData.length > MAX_IB_CAPACITY) {
            throw new IllegalArgumentException(
                    "newData is greater than length " + MAX_IB_CAPACITY +
                            ", which will cause a buffer overflow");
        }

        ib.clear();
        ib.limit(newData.length);
        ib.put(newData);
        ib.flip();
    }
}
