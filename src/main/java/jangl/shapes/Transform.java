package jangl.shapes;

import jangl.io.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {
    private final Matrix4f projectionMatrix;
    private final Matrix4f transformMatrix;
    private final Matrix4f rotationMatrix;
    private Vector3f center;
    private final Vector2f shift;

    public Transform() {
        this.center = new Vector3f(0, 0, 0);
        this.shift = new Vector2f(0, 0);
        this.projectionMatrix = new Matrix4f().ortho2D(0, (float) Window.getScreenWidth() / Window.getScreenHeight(), 0, 1);
        this.transformMatrix = new Matrix4f().identity();
        this.rotationMatrix = new Matrix4f().translate(this.center);
    }

    public void setPos(float x, float y) {
        Vector2f delta = new Vector2f(this.shift).sub(x, y).mul(-1);
        this.shift(delta.x, delta.y);
    }

    public void shift(float x, float y) {
        this.transformMatrix.translate(x, y, 0);
        this.shift.add(x, y);
    }

    public void rotate(float radians) {
        this.rotationMatrix.rotateZ(radians);
    }

    public void rotateOrigin(float radians) {
        this.transformMatrix.rotateZ(radians);
    }

    void setCenter(Vector2f center) {
        this.center = new Vector3f(center, 0);
        this.rotationMatrix.translate(this.center.x(), this.center.y(), 0);
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f getTransformMatrix() {
        return this.transformMatrix;
    }

    public Matrix4f getRotationMatrix() {
        return this.rotationMatrix;
    }

    public Matrix4f getMatrix() {
        return new Matrix4f()
                .set(this.projectionMatrix)
                .mul(this.transformMatrix)
                .mul(this.rotationMatrix)
                .mul(new Matrix4f().translate(new Vector3f(this.center).mul(-1)));
    }
}
