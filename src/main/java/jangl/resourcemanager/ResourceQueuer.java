package jangl.resourcemanager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Queues resources to be freed when appropriate.
 */
public class ResourceQueuer implements Runnable {
    private final Resource resource;
    private final AtomicBoolean isClosed;

    /**
     * @param isClosed If the resource is already closed.
     * @param resource The resource to be freed
     */
    public ResourceQueuer(AtomicBoolean isClosed, Resource resource) {
        this.resource = resource;
        this.isClosed = isClosed;
    }

    /**
     * Queue the resource to be freed.
     */
    @Override
    public void run() {
        // Do not queue the resource if it's already closed
        if (this.isClosed.get()) {
            return;
        }

        ResourceManager.queue(this.resource);
    }
}
