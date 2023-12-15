package jangl.memorymanager;

/**
 * Queues resources to be freed when appropriate.
 */
public class ResourceQueuer implements Runnable {
    private final Resource resource;

    /**
     * @param resource The resource to be freed
     */
    public ResourceQueuer(Resource resource) {
        this.resource = resource;
    }

    /**
     * Queue the resource to be freed.
     */
    @Override
    public void run() {
        ResourceManager.queue(this.resource);
    }
}
