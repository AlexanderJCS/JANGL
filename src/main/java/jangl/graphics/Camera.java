package jangl.graphics;

import jangl.coords.WorldCoords;
import jangl.io.Window;
import org.joml.*;

import static org.lwjgl.opengl.GL46.*;

public class Camera {
    public static final String UBO_CODE = "layout(binding = 83) uniform CameraPos {mat4 cameraMatrix;mat4 projectionMatrix;};";
    public static final int BINDING_POINT = 83;
    private static Matrix4f cameraMatrix;
    private static int uboID;

    private static boolean initialized = false;

    private Camera() {

    }

    public static void init() {
        if (initialized) {
            return;
        }

        // Create the uniform buffer object
        uboID = glGenBuffers();
        glBindBuffer(GL_UNIFORM_BUFFER, uboID);

        cameraMatrix = new Matrix4f().identity();
        Matrix4f projectionMatrix = new Matrix4f().ortho2D(0, (float) Window.getScreenWidth() / Window.getScreenHeight(), 0, 1);

        float[] combinedMatrix = new float[32];
        System.arraycopy(matrixToArray(cameraMatrix), 0, combinedMatrix, 0, 16);
        System.arraycopy(matrixToArray(projectionMatrix), 0, combinedMatrix, 16, 16);

        glBufferData(GL_UNIFORM_BUFFER, combinedMatrix, GL_DYNAMIC_DRAW);
        glBindBufferBase(GL_UNIFORM_BUFFER, BINDING_POINT, uboID);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

        initialized = true;
    }

    private static float[] matrixToArray(Matrix4f matrix) {
        float[] array = new float[16];
        matrix.get(array);

        return array;
    }

    private static void resetCameraMatrixUBO() {
        glBindBuffer(GL_UNIFORM_BUFFER, uboID);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, matrixToArray(cameraMatrix));
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
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
        Vector4f middle = new Vector4f(WorldCoords.getMiddle().toVector2f(), 0, 1);

        middle.mul(cameraMatrix);

        return new WorldCoords(middle.x, middle.y);
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
     * Rotate counterclockwise by a certain number of radians.
     * @param radians The delta to rotate counterclockwise by.
     */
    public static void rotate(float radians) {
        WorldCoords pos = getCenter();

        // Perform local rotation around the camera's bottom-left position
        cameraMatrix.translate(pos.x, pos.y, 0)
                .rotateZ(radians)
                .translate(-pos.x, -pos.y, 0);

        resetCameraMatrixUBO();
    }

    /**
     * Set the rotation to a certain amount.
     * @param radians The amount, in radians, to set the rotation to.
     */
    public static void setRotation(float radians) {
        float delta = radians - getRotation();
        rotate(delta);
    }


    public boolean getInit() {
        return initialized;
    }
}
