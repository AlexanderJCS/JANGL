package jangl.shapes;

import jangl.coords.WorldCoords;
import org.joml.*;

public class Transform {
    private final Matrix4f modelMatrix;
    private final Vector2f shift;
    private float localRotationAngle;
    private float originRotationAngle;

    public Transform() {
        this.shift = new Vector2f(0, 0);

        this.modelMatrix = new Matrix4f().identity();

        this.localRotationAngle = 0;
        this.originRotationAngle = 0;
    }

    /**
     * Sets the center of the object to the specific value.
     * @param x The x coordinate of the new center.
     * @param y The y coordinate of the new center.
     */
    public void setPos(float x, float y) {
        WorldCoords center = this.getCenter();
        this.shift(x - center.x, y - center.y);
    }

    public void setScale(float factor) {
        // currentScale * ? = factor
        // ? = factor / currentScale

        float delta = factor / this.getScale();

        WorldCoords center = this.getCenter();
        this.modelMatrix.scaleAroundLocal(delta, delta, 0, center.x, center.y, 0);
    }

    public float getScale() {
        Vector3f scale = new Vector3f();
        this.modelMatrix.getScale(scale);

        return scale.x;
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
        this.modelMatrix.translateLocal(x, y, 0);
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
        this.rotateRelative(radians, new WorldCoords(0, 0));
        this.localRotationAngle += radians;
    }

    /**
     * Rotate around the given point. If you want to rotate around the local center of the shape, it is better to use
     * the rotate() method. If you want to rotate around (0, 0), it is better to use rotateOrigin(). This is because
     * you can use the getLocalRotationAngle() and getOriginRotationAngle(), but you can't if you rotate from those points here.
     * <br>
     * WARNING: if you are rotating around multiple points at the same time, it is likely that you will come across
     * unintended effects. Make sure to only rotate around one point at a time. If you want to rotate around a point
     * relative to the object, use this.rotateRelative().
     *
     * @param radians The amount of radians to rotate.
     * @param origin  The origin of the point to rotate across.
     */
    public void rotateAround(float radians, WorldCoords origin) {
        WorldCoords centerNoRotation = this.getCenterNoRotation();

        this.modelMatrix.rotateAround(
                new Quaternionf().rotateZ(radians),
                (origin.x - centerNoRotation.x) / this.getScale(),
                (origin.y - centerNoRotation.y) / this.getScale(),
                0
        );
    }

    /**
     * Rotates relative to the point. Useful compared to rotateAround() if the object is constantly moving.
     * <br>
     * WARNING: changing the relative location may give unintended side effects.
     *
     * @param radians The amount to rotate counter-clockwise. Negative to rotate clockwise.
     * @param relativeLocation The relative location to rotate around. This is relative to the center of the object.
     */
    public void rotateRelative(float radians, WorldCoords relativeLocation) {
        this.modelMatrix.rotateAround(new Quaternionf().rotateZ(radians), relativeLocation.x, relativeLocation.y, 0);
    }

    /**
     * Rotates the object counterclockwise across the origin (bottom left of the screen). If you want to rotate the
     * object across its center instead, use the rotate(float) method.
     *
     * @param radians The amount, in radians, to rotate counterclockwise across the axis.
     */
    public void rotateOrigin(float radians) {
        this.rotateAround(radians, new WorldCoords(0, 0));
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
        Vector3f center = new Vector3f();
        this.modelMatrix.transformPosition(center);
        return new WorldCoords(center.x, center.y);
    }

    public WorldCoords getCenterNoRotation() {
        return new WorldCoords(this.shift.x, this.shift.y);
    }

    /**
     * Get a deepcopy of the transformation matrix. Primarily used to pass the matrix as a uniform to the GPU.
     *
     * @return The transformation matrix.
     * @deprecated Since the rotation matrix is removed, use this.getMatrix() instead of this method.
     */
    @Deprecated
    public Matrix4f getTransformMatrix() {
        return new Matrix4f(this.modelMatrix);
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
        return new Matrix4f(this.modelMatrix);
    }
}