package jangl.shapes;

import jangl.coords.WorldCoords;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Transform {
    private final Matrix4f transformMatrix;
    private final Vector2f shift;
    private Vector3f center;
    private float localRotationAngle;
    private float originRotationAngle;

    public Transform() {
        this.center = new Vector3f(0, 0, 0);
        this.shift = new Vector2f(0, 0);

        this.transformMatrix = new Matrix4f().identity();

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

    public void setScale(float factor) {
        // currentScale * ? = factor
        // ? = factor / currentScale

        float deltaX = factor / this.getScaleX();
        float deltaY = factor / this.getScaleY();

        this.transformMatrix.scaleAroundLocal(deltaX, deltaY, 0, this.center.x, this.center.y, 0);
    }

    public void setScaleX(float factor) {
        float deltaX = factor / this.getScaleX();
        this.transformMatrix.scaleAroundLocal(deltaX, 1, 0, this.center.x, this.center.y, 0);
    }

    public void setScaleY(float factor) {
        float deltaY = factor / this.getScaleY();
        this.transformMatrix.scaleAroundLocal(1, deltaY, 0, this.center.x, this.center.y, 0);
    }

    public Vector2f getScale() {
        Vector3f scale = new Vector3f();
        this.transformMatrix.getScale(scale);

        return new Vector2f(scale.x, scale.y);
    }

    public float getScaleX() {
        Vector3f scale = new Vector3f();
        this.transformMatrix.getScale(scale);

        return scale.x;
    }

    public float getScaleY() {
        Vector3f scale = new Vector3f();
        this.transformMatrix.getScale(scale);

        return scale.y;
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
        this.transformMatrix.rotateAround(new Quaternionf().rotateZ(radians), this.getCenter().x, this.getCenter().y, 0);
        this.localRotationAngle += radians;
    }

    /**
     * Rotate around the given point. If you want to rotate around the local center of the shape, it is better to use
     * the rotate() method. If you want to rotate around (0, 0), it is better to use rotateOrigin(). This is because
     * you can use the getLocalRotationAngle() and getOriginRotationAngle(), but you can't if you rotate from those points here.
     *
     * @param radians The amount of radians to rotate.
     * @param origin  The origin of the point to rotate across.
     */
    public void rotateAround(float radians, WorldCoords origin) {
        this.transformMatrix.rotateAround(new Quaternionf().rotateZ(radians), origin.x, origin.y, 0);
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
        this.transformMatrix.translate(center.x, center.y, 0);
    }

    /**
     * Get a deepcopy of the transformation matrix. Primarily used to pass the matrix as a uniform to the GPU.
     *
     * @return The transformation matrix.
     * @deprecated Since the rotation matrix is removed, use this.getMatrix() instead of this method.
     */
    @Deprecated
    public Matrix4f getTransformMatrix() {
        return new Matrix4f(this.transformMatrix);
    }

    /**
     * Get a deepcopy of the rotation matrix. Primarily used to pass the matrix as a uniform to the GPU.
     *
     * @return An identity matrix, since the rotation matrix is removed.
     * @deprecated The rotation matrix is removed.
     */
    @Deprecated
    public Matrix4f getRotationMatrix() {
        return new Matrix4f().identity();
    }

    /**
     * @return The multiplied rotation and transformation matrix. Due to matrix multiplications on the CPU potentially
     * being slow, it is not recommended to call this method often.
     */
    public Matrix4f getMatrix() {
        return new Matrix4f(this.transformMatrix);
    }
}