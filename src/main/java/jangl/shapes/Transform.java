package jangl.shapes;

import jangl.coords.WorldCoords;
import org.joml.*;

public class Transform {
   private final WorldCoords transform;
   private float rotation;
   private final Vector2f scale;
   private final Matrix4f modelMatrix;

    public Transform() {
        this.transform = new WorldCoords(0, 0);
        this.modelMatrix = new Matrix4f().identity();
        this.rotation = 0;
        this.scale = new Vector2f(1, 1);
    }

    /**
     * Sets the X and Y scale of the object to the given value.
     * @param factor The scale factor.
     */
    public void setScale(float factor) {
        this.scale.set(factor, factor);
    }

    /**
     * Sets the X scale of the object to the given value.
     * @param scaleX The X scale factor.
     */
    public void setScaleX(float scaleX) {
        this.scale.x = scaleX;
    }

    /**
     * Performs calculations given the unscaled width of the object to set the width of the object to the given value.
     *
     * @param desiredWidth The desired width of the object in WorldCoords
     * @param unscaledWidth The unscaled width of the object in WorldCoords
     */
    public void setWidth(float desiredWidth, float unscaledWidth) {
        float xScale = desiredWidth / unscaledWidth;
        this.setScaleX(xScale);
    }

    /**
     * Performs calculations given the unscaled height of the object to set the height of the object to the given value.
     *
     * @param desiredHeight The desired height of the object in WorldCoords
     * @param unscaledHeight The unscaled height of the object in WorldCoords
     */
    public void setHeight(float desiredHeight, float unscaledHeight) {
        float yScale = desiredHeight / unscaledHeight;
        this.setScaleY(yScale);
    }

    /**
     * Sets the Y scale of the object to the given value.
     * @param scaleY The Y scale factor.
     */
    public void setScaleY(float scaleY) {
        this.scale.y = scaleY;
    }

    public float getScaleX() {
        return this.scale.x;
    }

    public float getScaleY() {
        return this.scale.y;
    }

    public Vector2f getScale() {
        return new Vector2f(this.scale);
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
        this.transform.add(x, y);
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
        this.rotation += radians;
    }

    /**
     * Sets the object's local rotation.
     *
     * @param radians The amount of radians to be rotated.
     */
    public void setRotation(float radians) {
        float delta = radians - this.rotation;
        this.rotate(delta);
    }

    /**
     * @return The local rotation angle in radians.
     */
    public float getRotation() {
        return this.rotation;
    }

    /**
     * @return The center of the object in WorldCoords.
     */
    public WorldCoords getCenter() {
        Vector3f center = new Vector3f();
        this.getMatrix().transformPosition(center);
        return new WorldCoords(center.x, center.y);
    }

    /**
     * @return The center of the object if no rotation is applied.
     */
    public WorldCoords getCenterNoRotation() {
        return new WorldCoords(this.transform.x, this.transform.y);
    }

    /**
     * @return The multiplied rotation and transformation matrix. Due to matrix multiplications on the CPU potentially
     * being slow, it is not recommended to call this method often.
     */
    public Matrix4f getMatrix() {
        return this.modelMatrix.identity()
                .translate(this.transform.x, this.transform.y, 0)
                .rotateAround(new Quaternionf().rotateZ(this.rotation), 0, 0, 0)
                .scale(this.scale.x, this.scale.y, 1);
    }
}