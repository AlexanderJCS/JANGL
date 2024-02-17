package jangl.graphics;

import jangl.coords.WorldCoords;
import jangl.graphics.shaders.UBO;
import jangl.io.Window;
import jangl.util.ArrayUtils;
import org.joml.*;

import static org.lwjgl.opengl.GL41.*;

public class Camera {
    public static final String UBO_CODE = "layout(std140) uniform Matrices {mat4 cameraMatrix;mat4 projectionMatrix;};";
    public static final int BINDING_POINT = UBO.getMaxBindingPoint();
    private static WorldCoords cameraPos;
    private static float rotation;
    private static float zoom;

    /**
     * The rightmost coordinate of the camera view. Used to check if the projection matrix needs to be updated.
     */
    private static float projMatrixRight;
    private static UBO ubo;
    private static WorldCoords zoomOffset;

    private static boolean initialized = false;

    private Camera() {

    }

    public static void init() {
        if (initialized) {
            return;
        }

        // Define camera-related values
        cameraPos = new WorldCoords(0, 0);
        rotation = 0;
        zoom = 1;

        // Define some projection matrix thingies
        projMatrixRight = WorldCoords.getTopRight().x;

        // Create the matrices
        Matrix4f cameraMatrix = genCameraMatrix();
        Matrix4f projectionMatrix = genProjMatrix();

        // Create the UBO
        float[] combinedMatrix = new float[32];
        System.arraycopy(ArrayUtils.matrixToArray(cameraMatrix), 0, combinedMatrix, 0, 16);
        System.arraycopy(ArrayUtils.matrixToArray(projectionMatrix), 0, combinedMatrix, 16, 16);
        ubo = new UBO(combinedMatrix, BINDING_POINT);

        initialized = true;
    }

    public boolean getInit() {
        return initialized;
    }

    /**
     * Adjusts the given world coordinates to be adjusted for the camera. E.g., if the camera's top right is at (2, 2),
     * but WorldCoords.getTopRight() is at (1, 1), then the given world coordinates will be adjusted to be at (2, 2).
     *
     * @param worldCoords The world coordinates to adjust. This method does not mutate this variable.
     * @return The adjusted world coordinates.
     */
    public static WorldCoords adjustForCamera(WorldCoords worldCoords) {
        Vector4f worldCoordsVec = new Vector4f(worldCoords.x, worldCoords.y, 0, 1);
        Matrix4f cameraMatrix = genCameraMatrix();
        worldCoordsVec.mul(cameraMatrix.invert());

        WorldCoords pos = new WorldCoords(worldCoordsVec.x, worldCoordsVec.y);
//        WorldCoords zoomOffsetNonNull = getZoomOffsetNonNull();
//        pos.sub(zoomOffsetNonNull.x * (1 - zoom) / zoom, zoomOffsetNonNull.y * (1 - zoom) / zoom);

        return pos;
    }

    /**
     * Updates the projection matrix if the screen size has changed, to not distort the image.
     */
    public static void update() {
        if (WorldCoords.getTopRight().x == projMatrixRight) {
            return;
        }

        Matrix4f projectionMatrix = genProjMatrix();
        resetProjMatrixUBO(projectionMatrix);
        projMatrixRight = WorldCoords.getTopRight().x;
    }

    /**
     * When zooming in when the offset is set to 0, 0, the zoom will be centered on the bottom left. By default, the
     * zoom offset is set to WorldCoords.getMiddle(), so the zoom will be centered in the middle of the screen.
     * <br>
     * Alternatively, you can set the zoom offset here to a different value, useful if you want to zoom in on a specific
     * location. If you want to reset the zoom offset to the middle of the screen, pass null to this method or call
     * setZoomOffsetDefault(). The advantage of setting it to null instead of WorldCoords.getMiddle() is that it will
     * be recalculated if the screen size changes.
     *
     * @param offset The offset to set the zoom to. Null to set it back to default.
     */
    public static void setZoomOffset(WorldCoords offset) {
        zoomOffset = offset;
    }

    /**
     * Resets the zoom offset to the default: WorldCoords.getMiddle().
     */
    public static void setZoomOffsetDefault() {
        setZoomOffset(null);
    }

    /**
     * @return The current zoom offset, or null if the zoom offset is set to the default of WorldCoords.getMiddle().
     */
    public static WorldCoords getZoomOffset() {
        if (zoomOffset == null) {
            return null;
        }

        return new WorldCoords(zoomOffset);
    }

    /**
     * @return The current zoom offset, or WorldCoords.getMiddle() if the zoom offset is set to the default.
     */
    public static WorldCoords getZoomOffsetNonNull() {
        return zoomOffset == null ? WorldCoords.getMiddle() : new WorldCoords(zoomOffset);
    }

    /**
     * Generates the camera matrix based on the cameraPos, rotation, and zoom static variables.
     * @return The camera matrix.
     */
    private static Matrix4f genCameraMatrix() {
        // A local variable of zoom offset that avoids the value being null
        WorldCoords zoomOffsetNonNull = getZoomOffsetNonNull();

        return new Matrix4f().identity()
                .translate(new Vector3f(cameraPos.toVector2f(), 0).mul(-1).mul(zoom, zoom, 1))
                // Translate to the center of the screen, zoom, then translate back, so the zoom is centered on the screen
                .translate(new Vector3f(zoomOffsetNonNull.toVector2f(), 0))
                .scale(zoom, zoom, 1)
                .translate(new Vector3f(zoomOffsetNonNull.toVector2f(), 0).mul(-1))
                .rotateAround(new Quaternionf().rotateZ(rotation), Camera.getCenter().x, Camera.getCenter().y, 0);
    }

    /**
     * Resets the camera matrix data in the UBO.
     * @param cameraMatrix The new camera matrix.
     */
    private static void resetCameraMatrixUBO(Matrix4f cameraMatrix) {
        ubo.bind();
        glBufferSubData(GL_UNIFORM_BUFFER, 0, ArrayUtils.matrixToArray(cameraMatrix));
        ubo.unbind();
    }

    /**
     * Generates the projection matrix based on the screen aspect ratio.
     * @return The projection matrix.
     */
    private static Matrix4f genProjMatrix() {
        return new Matrix4f().ortho2D(
                // Left and right
                0, (float) Window.getScreenWidth() / Window.getScreenHeight(),
                // Bottom and top
                0, 1
        );
    }

    /**
     * Resets the projection matrix data in the UBO.
     * @param projectionMatrix The new projection matrix.
     */
    private static void resetProjMatrixUBO(Matrix4f projectionMatrix) {
        ubo.bind();
        glBufferSubData(GL_UNIFORM_BUFFER, 16 * Float.BYTES, ArrayUtils.matrixToArray(projectionMatrix));
        ubo.unbind();
    }

    public static UBO getUbo() {
        return ubo;
    }

    /**
     * Sets the bottom left coordinate of the camera view.
     *
     * @param bottomLeft The bottom left coordinate of the camera view.
     */
    public static void setCameraPos(WorldCoords bottomLeft) {
        cameraPos = new WorldCoords(bottomLeft);

        resetCameraMatrixUBO(genCameraMatrix());
    }

    /**
     * @return The bottom left coordinate of the camera view if the camera were not rotated.
     */
    public static WorldCoords getCameraPos() {
        return new WorldCoords(cameraPos.x, cameraPos.y);
    }

    /**
     * Sets the center coordinate of the camera view. Set to WorldCoords.getMiddle() by default.
     * @param center The center coordinate of the camera's view.
     */
    public static void setCenter(WorldCoords center) {
        WorldCoords screenCenter = WorldCoords.getMiddle();
        setCameraPos(new WorldCoords(center.x - screenCenter.x, center.y - screenCenter.y));

        resetCameraMatrixUBO(genCameraMatrix());
    }

    /**
     * @return The center coordinate of the camera view
     */
    public static WorldCoords getCenter() {
        return new WorldCoords(
                getCameraPos().x + WorldCoords.getMiddle().x / zoom,
                getCameraPos().y + WorldCoords.getMiddle().y / zoom
        );
    }

    /**
     * @return The rotation angle in radians
     */
    public static float getRotation() {
        return rotation;
    }

    /**
     * Set the rotation to a certain amount.
     * @param radians The angle, in radians, to set the rotation to.
     */
    public static void setRotation(float radians) {
        rotation = radians;
        resetCameraMatrixUBO(genCameraMatrix());
    }

    /**
     * Rotate counterclockwise by a certain number of radians.
     * @param radians The delta to rotate counterclockwise by.
     */
    public static void rotate(float radians) {
        rotation += radians;
        resetCameraMatrixUBO(genCameraMatrix());
    }

    /**
     * Set the zoom to a new zoom multiplier.
     * @param newZoom The new zoom.
     */
    public static void setZoom(float newZoom) {
        zoom = newZoom;
        resetCameraMatrixUBO(genCameraMatrix());
    }

    /**
     * Gets the current zoom multiplier. 1 = normal zoom, 2 = double zoom, 0.5 = 50% zoom, etc.
     * @return The current zoom factor.
     */
    public static float getZoom() {
        return zoom;
    }
}
