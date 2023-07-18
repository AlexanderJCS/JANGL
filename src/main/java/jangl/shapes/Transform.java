package jangl.shapes;

import jangl.coords.WorldCoords;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {
    private final Matrix4f transformMatrix;
    private final Matrix4f rotationMatrix;
    private Vector3f center;
    private final Vector2f shift;

    private float localRotationAngle;
    private float originRotationAngle;

    public Transform() {
        this.center = new Vector3f(0, 0, 0);
        this.shift = new Vector2f(0, 0);

        this.transformMatrix = new Matrix4f().identity();
        this.rotationMatrix = new Matrix4f();

        this.localRotationAngle = 0;
        this.originRotationAngle = 0;
    }

    public void setPos(float x, float y) {
        Vector2f delta = new Vector2f(this.shift).sub(x, y).add(this.center.x(), this.center.y()).mul(-1);
        this.shift(delta.x, delta.y);
    }

    public void shift(float x, float y) {
        this.transformMatrix.translate(x, y, 0);
        this.shift.add(x, y);
    }

    public void rotate(float radians) {
        this.rotationMatrix.rotateZ(radians);
        this.localRotationAngle += radians;
    }

    public void rotateOrigin(float radians) {
        this.transformMatrix.rotateZ(radians);
        this.originRotationAngle += radians;
    }

    public void setLocalRotation(float radians) {
        float delta = radians - this.localRotationAngle;
        this.rotate(delta);
    }

    public void setOriginRotation(float radians) {
        float delta = radians - this.originRotationAngle;
        this.rotateOrigin(delta);
    }

    void setCenter(Vector2f center) {
        this.center = new Vector3f(center, 0);
        this.rotationMatrix.translate(this.center.x(), this.center.y(), 0);
    }

    public WorldCoords getCenter() {
        Vector2f center = new Vector2f(this.center.x(), this.center.y()).add(this.shift);
        return new WorldCoords(center.x, center.y);
    }

    public Matrix4f getTransformMatrix() {
        return this.transformMatrix;
    }

    public Matrix4f getRotationMatrix() {
        return this.rotationMatrix;
    }

    public float getLocalRotationAngle() {
        return localRotationAngle;
    }

    public float getOriginRotationAngle() {
        return originRotationAngle;
    }

    public Matrix4f getMatrix() {
        return new Matrix4f()
                .set(this.getTransformMatrix())
                .mul(this.getRotationMatrix());
    }
}