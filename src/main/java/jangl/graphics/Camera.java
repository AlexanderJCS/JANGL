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
    private static Matrix4f cameraMatrix;

    /**
     * The rightmost coordinate of the camera view. Used to check if the projection matrix needs to be updated.
     */
    private static float projMatrixRight;
    private static UBO ubo;
    private static float zoom = 1;

    private static boolean initialized = false;

    private Camera() {

    }

    public static void init() {
        if (initialized) {
            return;
        }

        // Create the uniform buffer object
        cameraMatrix = new Matrix4f().identity();
        Matrix4f projectionMatrix = genProjMatrix();
        projMatrixRight = WorldCoords.getTopRight().x;

        float[] combinedMatrix = new float[32];
        System.arraycopy(ArrayUtils.matrixToArray(cameraMatrix), 0, combinedMatrix, 0, 16);
        System.arraycopy(ArrayUtils.matrixToArray(projectionMatrix), 0, combinedMatrix, 16, 16);
        ubo = new UBO(combinedMatrix, BINDING_POINT);

        initialized = true;
    }

    private static void resetCameraMatrixUBO() {
        ubo.bind();
        glBufferSubData(GL_UNIFORM_BUFFER, 0, ArrayUtils.matrixToArray(cameraMatrix));
        ubo.unbind();
    }

    private static Matrix4f genProjMatrix() {
        return new Matrix4f().ortho2D(0, (float) Window.getScreenWidth() / Window.getScreenHeight(), 0, 1);
    }

    private static void resetProjectionMatrixUBO(Matrix4f projectionMatrix) {
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
        cameraMatrix.setTranslation(new Vector3f(bottomLeft.toVector2f(), 0).mul(-1));
        resetCameraMatrixUBO();
    }

    /**
     * @return The bottom left coordinate of the camera view if the camera were not rotated.
     */
    public static WorldCoords getCameraPos() {
        Vector3f transform = new Vector3f();
        cameraMatrix.getTranslation(transform);

        return new WorldCoords(-1 * transform.x, -1 * transform.y);
    }

    /**
     * @return The center coordinate of the camera view
     */
    public static WorldCoords getCenter() {
        WorldCoords screenMiddle = WorldCoords.getMiddle();
        WorldCoords cameraPos = getCameraPos();

        return new WorldCoords(cameraPos.x + screenMiddle.x, cameraPos.y + screenMiddle.y);
    }

    public static void setCenter(WorldCoords center) {
        WorldCoords screenCenter = WorldCoords.getMiddle();
        setCameraPos(new WorldCoords(center.x - screenCenter.x, center.y - screenCenter.y));
    }

    public static float getRotation() {
        // Extract the rotation from the camera matrix
        AxisAngle4f rotationVec = new AxisAngle4f();
        cameraMatrix.getRotation(rotationVec);

        return rotationVec.angle;
    }

    /**
     * Set the rotation to a certain amount.
     * @param radians The amount, in radians, to set the rotation to.
     */
    public static void setRotation(float radians) {
        float delta = radians - getRotation();
        rotate(delta);
    }

    /**
     * Rotate counterclockwise by a certain number of radians.
     * @param radians The delta to rotate counterclockwise by.
     */
    public static void rotate(float radians) {
        cameraMatrix.rotateAround(new Quaternionf().rotateZ(radians), Camera.getCenter().x, Camera.getCenter().y, 0);

        resetCameraMatrixUBO();
    }

    /**
     * Set the zoom to a new zoom multiplier.
     * @param newZoom The new zoom.
     */
    public static void setZoom(float newZoom) {
        float delta = newZoom / getZoom();
        zoom = newZoom;

        WorldCoords center = getCenter();

        cameraMatrix.scaleAroundLocal(delta, center.x, center.y, 0);
        resetCameraMatrixUBO();
    }

    /**
     * Gets the current zoom multiplier. 1 = normal zoom, 2 = double zoom, 0.5 = 50% zoom, etc.
     * @return The current zoom factor.
     */
    public static float getZoom() {
        return zoom;
    }

    public boolean getInit() {
        return initialized;
    }

    /**
     * Updates the projection matrix if the screen size has changed, as to not distort the image.
     */
    public static void update() {
        if (WorldCoords.getTopRight().x == projMatrixRight) {
            return;
        }

        Matrix4f projectionMatrix = genProjMatrix();
        resetProjectionMatrixUBO(projectionMatrix);
        projMatrixRight = WorldCoords.getTopRight().x;
    }
}
