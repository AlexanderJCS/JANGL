package jangl.graphics.batching;

import jangl.shapes.Rect;

import java.util.ArrayList;
import java.util.List;

public class BatchBuilder {
    private final List<Float> vertices;
    private final List<Integer> indices;
    private final List<Float> texCoords;

    public BatchBuilder() {
        this.vertices = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.texCoords = new ArrayList<>();
    }

    /**
     * Add a rectangle to the batch.
     * @param rect The rectangle to add.
     * @return this object, allowing for method chaining
     */
    public BatchBuilder addObject(Rect rect) {
        this.addObject(
                rect,
                rect.getTexCoords()
        );

        return this;
    }

    /**
     * Add a rectangle to the batch with custom texture coords.
     * @param rect The rectangle to add.
     * @param texCoords The custom texture coords.
     * @return this object, allowing for method chaining
     */
    public BatchBuilder addObject(Rect rect, float[] texCoords) {
        this.addObject(
                rect.calculateVerticesMatrix(),
                rect.getIndices(),
                texCoords
        );

        return this;
    }

    /**
     * Add the vertices, indices, and texture coords into the batch manually.
     * You do not need to adjust the indices for the vertices that are already in the batch,
     * the program does that automatically.
     *
     * @param vertices  The vertices to add.
     * @param indices   The indices to add.
     * @param texCoords The texture coords to add to the object.
     * @return this object, allowing for method chaining
     */
    public BatchBuilder addObject(float[] vertices, int[] indices, float[] texCoords) {
        int indicesOffset = this.vertices.size() / 2;

        for (float vertex : vertices) {
            this.vertices.add(vertex);
        }


        for (int index : indices) {
            this.indices.add(index + indicesOffset);
        }

        for (float texCoord : texCoords) {
            this.texCoords.add(texCoord);
        }

        return this;
    }

    /**
     * Combine two batch builders into one.
     * @param other The other batch to add.
     * @return this object, allowing for method chaining
     */
    public BatchBuilder addObject(BatchBuilder other) {
        this.addObject(
                other.getVertices(),
                other.getIndices(),
                other.getTexCoords()
        );

        return this;
    }

    public float[] getVertices() {
        float[] vertices = new float[this.vertices.size()];

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = this.vertices.get(i);
        }

        return vertices;
    }

    public int[] getIndices() {
        int[] indices = new int[this.indices.size()];

        for (int i = 0; i < indices.length; i++) {
            indices[i] = this.indices.get(i);
        }

        return indices;
    }

    public float[] getTexCoords() {
        float[] texCoords = new float[this.texCoords.size()];

        for (int i = 0; i < texCoords.length; i++) {
            texCoords[i] = this.texCoords.get(i);
        }

        return texCoords;
    }
}
