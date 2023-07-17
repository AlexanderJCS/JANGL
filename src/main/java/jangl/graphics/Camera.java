package jangl.graphics;

import static org.lwjgl.opengl.GL46.*;

import jangl.coords.WorldCoords;

public class Camera {
    private static WorldCoords center = new WorldCoords(0, 0);
    public static final int BINDING_POINT = 83;
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
        glBufferData(GL_UNIFORM_BUFFER, new float[]{center.x, center.y}, GL_DYNAMIC_DRAW);
        glBindBufferBase(GL_UNIFORM_BUFFER, BINDING_POINT, uboID);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

        initialized = true;
    }

    public boolean getInit() {
        return initialized;
    }

    public static void setCenter(WorldCoords center) {
        Camera.center = center;

        glBindBuffer(GL_UNIFORM_BUFFER, uboID);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, new float[]{center.x, center.y});
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public static WorldCoords getCenter(WorldCoords center) {
        return new WorldCoords(center.x, center.y);
    }
}
