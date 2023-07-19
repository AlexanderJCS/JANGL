package jangl.graphics;

import jangl.coords.WorldCoords;
import jangl.io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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

    public static void setCameraPos(WorldCoords bottomLeft) {
        cameraMatrix.setTranslation(new Vector3f(bottomLeft.toVector2f(), 0).mul(-1));

        glBindBuffer(GL_UNIFORM_BUFFER, uboID);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, matrixToArray(cameraMatrix));
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public boolean getInit() {
        return initialized;
    }
}
