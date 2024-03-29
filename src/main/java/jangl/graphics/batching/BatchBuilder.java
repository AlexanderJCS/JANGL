package jangl.graphics.batching;

import jangl.coords.WorldCoords;
import jangl.shapes.Rect;
import jangl.shapes.Shape;
import jangl.shapes.Triangle;

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
     * Adds a shape to the batch.
     * @param shape The shape to add.
     * @return this object, allowing for method chaining
     */
    public BatchBuilder addObject(Shape shape) {
        this.addObject(
                shape,
                shape.getTexCoords()
        );

        return this;
    }

    /**
     * Add a shape to the batch with custom texture coords.
     * @param shape The shape to add.
     * @param texCoords The custom texture coords.
     * @return this object, allowing for method chaining
     */
    public BatchBuilder addObject(Shape shape, float[] texCoords) {
        this.addObject(
                shape.calculateVerticesMatrix(),
                shape.getIndices(),
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

    /**
     * Get the vertices where (0, 0) is the center of all the objects.
     * @return The vertices where (0, 0) is the center of all the objects.
     */
    public float[] getVerticesLocal() {
        WorldCoords middle = this.getMiddle();
        float[] vertices = this.getVertices();

        for (int i = 0; i < vertices.length; i++) {
            // If the index is even the vertex is an x coordinate
            if (i % 2 == 0) {
                vertices[i] -= middle.x;
            } else {
                vertices[i] -= middle.y;
            }
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

    /**
     * Calculates the middle point of the vertices.
     * @return The middle point of the vertices.
     */
    public WorldCoords getMiddle() {
        float sumX = 0;
        float sumY = 0;

        for (int i = 0; i < this.vertices.size(); i++) {
            // If the index is even it must be an x coordinate
            if (i % 2 == 0) {
                sumX += this.vertices.get(i);
            } else {
                sumY += this.vertices.get(i);
            }
        }

        return new WorldCoords(
                sumX / this.vertices.size() * 2,
                sumY / this.vertices.size() * 2
        );
    }

    /**
     * Creates a new batch with the given characteristics of this BatchBuilder.
     * This method is the same thing as new Batch(this).
     *
     * @return The batch with the characteristics of this BatchBuilder
     */
    public Batch toBatch() {
        return new Batch(this);
    }
}
