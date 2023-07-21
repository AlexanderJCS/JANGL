package jangl.shapes;

import jangl.coords.WorldCoords;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {
    private final Matrix4f transformMatrix;
    private final Matrix4f rotationMatrix;
    private final Vector2f shift;
    private Vector3f center;
    private float localRotationAngle;
    private float originRotationAngle;

    public Transform() {
        this.center = new Vector3f(0, 0, 0);
        this.shift = new Vector2f(0, 0);

        this.transformMatrix = new Matrix4f().identity();
        this.rotationMatrix = new Matrix4f().identity();

        this.localRotationAngle = 0;
        this.originRotationAngle = 0;
    }

    /**
     * Sets the center of the object to the specific value.
     * @param x The x coordinate of the new center.
     * @param y The y coordinate of the new center.
     */
    public void setPos(float x, float y) {
        Vector2f delta = new Vector2f(this.shift).sub(x, y).add(this.center.x(), this.center.y()).mul(-1);
        this.shift(delta.x, delta.y);
    }

    /**
     * Sets the center of the object to the specific value.
     * @param pos The new position.
     */
    public void setPos(WorldCoords pos) {
        this.setPos(pos.x, pos.y);
    }

    /**
     * Changes the object's position by a specified amount.
     *
     * @param x The x delta to move.
     * @param y The y delta to move.
     */
    public void shift(float x, float y) {
        this.transformMatrix.translate(x, y, 0);
        this.shift.add(x, y);
    }

    /**
     * Changes the object's position by a specified amount.
     *
     * @param shiftCoords The amount to shift the object by.
     */
    public void shift(WorldCoords shiftCoords) {
        this.shift(shiftCoords.x, shiftCoords.y);
    }

    /**
     * Rotates the object counterclockwise by a certain amount of radians.
     *
     * @param radians The amount, in radians, to rotate the object by.
     */
    public void rotate(float radians) {
        this.rotationMatrix.rotateZ(radians);
        this.localRotationAngle += radians;
    }

    /**
     * Rotates the object counterclockwise across the origin (bottom left of the screen). If you want to rotate the
     * object across its center instead, use the rotate(float) method.
     *
     * @param radians The amount, in radians, to rotate counterclockwise across the axis.
     */
    public void rotateOrigin(float radians) {
        this.transformMatrix.rotateZ(radians);
        this.originRotationAngle += radians;
    }

    /**
     * Sets the object's local rotation.
     *
     * @param radians The amount of radians to be rotated.
     */
    public void setLocalRotation(float radians) {
        float delta = radians - this.localRotationAngle;
        this.rotate(delta);
    }

    /**
     * @return The local rotation angle.
     */
    public float getLocalRotationAngle() {
        return localRotationAngle;
    }

    /**
     * Sets the object's rotation across the origin (bottom left of the screen).
     *
     * @param radians The amount of radians to be rotated.
     */
    public void setOriginRotation(float radians) {
        float delta = radians - this.originRotationAngle;
        this.rotateOrigin(delta);
    }

    /**
     * @return The angle of rotation across the origin (bottom left of the screen).
     */
    public float getOriginRotationAngle() {
        return originRotationAngle;
    }

    /**
     * @return The center of the object in WorldCoords.
     */
    public WorldCoords getCenter() {
        Vector2f center = new Vector2f(this.center.x(), this.center.y()).add(this.shift);
        return new WorldCoords(center.x, center.y);
    }

    /**
     * Used only right after creating the object to set the center of the object.
     *
     * @param center The center of the object.
     */
    void setCenter(Vector2f center) {
        this.center = new Vector3f(center, 0);
        this.rotationMatrix.translate(this.center.x(), this.center.y(), 0);
    }

    /**
     * Get a deepcopy of the transformation matrix. Primarily used to pass the matrix as a uniform to the GPU.
     *
     * @return The transformation matrix.
     */
    public Matrix4f getTransformMatrix() {
        return new Matrix4f(this.transformMatrix);
    }

    /**
     * Get a deepcopy of the rotation matrix. Primarily used to pass the matrix as a uniform to the GPU.
     *
     * @return The transformation matrix.
     */
    public Matrix4f getRotationMatrix() {
        return new Matrix4f(this.rotationMatrix);
    }

    /**
     * @return The multiplied rotation and transformation matrix. Due to matrix multiplications on the CPU potentially
     * being slow, it is not recommended to call this method often.
     */
    public Matrix4f getMatrix() {
        return new Matrix4f()
                .set(this.getTransformMatrix())
                .mul(this.getRotationMatrix());
    }
}