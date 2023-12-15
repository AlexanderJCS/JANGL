package jangl.resourcemanager;

public class Resource {
    private final int[] resource;
    private final ResourceType type;

    /**
     * @param resource The OpenGL IDs of the resources to be freed.
     * @param type The type of resource to be freed.
     */
    public Resource(int[] resource, ResourceType type) {
        this.resource = resource;
        this.type = type;
    }


    /**
     * @param resource The OpenGL ID of the resource to be freed.
     * @param type The type of resource to be freed.
     */
    public Resource(int resource, ResourceType type) {
        this.resource = new int[]{ resource };
        this.type = type;
    }

    public int[] getResource() {
        return this.resource;
    }

    public ResourceType getType() {
        return this.type;
    }
}
