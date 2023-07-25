package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.models.Model;
import jangl.graphics.models.TriangleFanModel;

import java.util.Arrays;

public class Circle extends Shape {
    private final int sides;
    private float radius;

    /**
     * Since one ScreenCoord on the x-axis is not the same as one ScreenCoord on the y-axis if the aspect ratio
     * is not 1:1, the radius will be in the units of X-axis WorldCoords.
     *
     * @param center The center of the circle.
     * @param radius The X-radius of the circle (see the above note)
     * @param sides  The number of sides of the shape.
     * @throws IllegalArgumentException Throws if the number of sides on the circle is less than 3.
     */
    public Circle(WorldCoords center, float radius, int sides) throws IllegalArgumentException {
        if (sides <= 2) {
            throw new IllegalArgumentException("A circle must have 3 or more sides, not " + sides);
        }

        this.sides = sides;
        this.radius = radius;
        this.model = this.toModel();
        this.transform.setCenter(center.toVector2f());
    }

    public int getSides() {
        return this.sides;
    }

    public float getRadius() {
        return this.radius;
    }

    /**
     * @param newRadius The new radius
     */
    public void setRadius(float newRadius) {
        this.radius = newRadius;
        this.model.subVertices(this.calculateVertices(), 0);
    }

    private Model toModel() {
        return new TriangleFanModel(this.calculateVertices());
    }

    @Override
    public void draw() {
        if (super.shouldDraw()) {
            super.draw();
            this.model.render();
        }
    }

    @Override
    public float[] calculateVertices() {
        float[] vertices = new float[2 * (this.sides + 2)];

        vertices[0] = 0;
        vertices[1] = 0;

        for (int i = 1; i < vertices.length / 2; i++) {
            vertices[i * 2] = (float) (this.radius * Math.cos(i * 2 * Math.PI / this.sides));
            vertices[i * 2 + 1] = (float) (this.radius * Math.sin(i * 2 * Math.PI / this.sides));
        }

        return vertices;
    }

    @Override
    public float[] getExteriorVertices() {
        float[] vertices = this.calculateVerticesMatrix();
        return Arrays.copyOfRange(vertices, 2, vertices.length - 2);
    }

    @Override
    public void close() {
        this.model.close();
    }
}
