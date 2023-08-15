package jangl.graphics.shaders;

import jangl.graphics.Bindable;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL41.*;

/**
 * A way to interface with OpenGL uniform buffer objects.
 */
public class UBO implements Bindable, AutoCloseable {
    private final int id;
    private final int bindingPoint;
    private static final Set<Integer> bindingPoints = new HashSet<>();

    /**
     * @param data The data in the UBO
     * @param bindingPoint The unique binding point of the UBO. Binding point 83 is taken by the view/projection matrix.
     * @throws IllegalArgumentException If the bindingPoint is greater than 84, less than 0, or equal to an already existing binding point.
     */
    public UBO(float[] data, int bindingPoint) throws IllegalArgumentException {
        if (bindingPoint < 0 || bindingPoint > 74) {
            throw new IllegalArgumentException(
                    "Binding point must be within the range of [0, 74]. In Windows and Linux," +
                    "the binding point may go up to 83 (inclusive), but to be cross compatible, JANGL does not allow" +
                    "you to have a binding point greater than 74 (inclusive)."
            );
        }

        if (bindingPoints.contains(bindingPoint)) {
            throw new IllegalArgumentException("Cannot create a UBO with a binding point that already exists");
        }

        this.bindingPoint = bindingPoint;
        this.id = glGenBuffers();

        this.bind();
        glBufferData(GL_UNIFORM_BUFFER, data, GL_DYNAMIC_DRAW);
        this.unbind();

        glBindBufferRange(GL_UNIFORM_BUFFER, bindingPoint, this.getID(), 0, (long) data.length * Float.BYTES);

        bindingPoints.add(bindingPoint);
    }

    public int getID() {
        return this.id;
    }

    public int getBindingPoint() {
        return this.bindingPoint;
    }

    @Override
    public void bind() {
        glBindBuffer(GL_UNIFORM_BUFFER, this.id);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    @Override
    public void close() {
        glDeleteBuffers(this.id);
    }
}
