package jangl.memorymanager;

import static org.lwjgl.opengl.GL41.*;

import java.lang.ref.Cleaner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ResourceManager {
    private static boolean initialized = false;
    private static final ConcurrentLinkedQueue<Resource> resourceQueue = new ConcurrentLinkedQueue<>();
    private static final Cleaner cleaner = Cleaner.create();

    private ResourceManager() {}

    public static void init() {
        if (initialized) {
            return;
        }

        initialized = true;
    }

    public static void add(Object resource, ResourceQueuer queuer) {
        cleaner.register(resource, queuer);
    }

    /**
     * Adds a resource to the queue to be freed.
     */
    public static void queue(Resource resource) {
        resourceQueue.add(resource);
    }

    /**
     * Free resources in queue to be deleted.
     */
    public static void freeResources() {
        Resource resource;

        while ((resource = resourceQueue.poll()) != null) {
            if (resource.getType() == ResourceType.BUFFER) {
                glDeleteBuffers(resource.getResource());
            }

            else if (resource.getType() == ResourceType.SHADER) {
                for (int shader : resource.getResource()) {
                    glDeleteShader(shader);
                }
            }

            else if (resource.getType() == ResourceType.VAO) {
                glDeleteVertexArrays(resource.getResource());
            }
        }
    }
}
