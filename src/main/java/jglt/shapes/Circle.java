package jglt.shapes;

import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.Model;

import java.util.ArrayList;
import java.util.List;

public class Circle implements AutoCloseable {
    private ScreenCoords center;
    private int sides;
    private float radiusX;
    private float radiusY;
    private Model model;

    /**
     * Since one ScreenCoord on the x-axis is not the same as one ScreenCoord on the y-axis if the aspect ratio
     * is not 1:1, the radius will be in the units of X-axis ScreenCoords.
     *
     * @param center The center of the circle.
     * @param radius The X-radius of the circle (see the above note)
     * @param sides The number of sides of the shape.
     */
    public Circle(ScreenCoords center, float radius, int sides) {
        this.center = center;

        this.sides = sides;
        this.radiusX = radius;
        this.radiusY = PixelCoords.distYToScreenDist(ScreenCoords.distXtoPixelCoords(radius));
        this.model = this.toModel();
    }

    public void setCenter(ScreenCoords newCenter) {
        this.center = newCenter;
        this.model = this.toModel();
    }

    /**
     * @param newRadius The new radius
     */
    public void setRadius(float newRadius) {
        this.radiusX = newRadius;
        this.radiusY = PixelCoords.distYToScreenDist(ScreenCoords.distXtoPixelCoords(newRadius));
        this.model = this.toModel();
    }

    public void setSides(int newSides) {
        this.sides = newSides;
        this.model = this.toModel();
    }

    public ScreenCoords getCenter() {
        return this.center;
    }

    public int getSides() {
        return this.sides;
    }

    public float getRadius() {
        return this.radiusX;
    }

    private Model toModel() {
        List<Float> vertices = new ArrayList<>();
        double angle = 0;  // radians

        while (angle < 2 * Math.PI) {
            float x = (float) (this.radiusX * Math.sin(angle));
            float y = (float) (this.radiusY * Math.cos(angle));

            float offsetX = x + this.center.x;
            float offsetY = y + this.center.y;

            // Duplicate the vertex if it's not the first triangle. This will be the start of the next triangle
            for (int i = 0; i < (vertices.size() > 2 ? 2 : 1); i++) {
                vertices.add(offsetX);
                vertices.add(offsetY);
            }

            vertices.add(this.center.x);
            vertices.add(this.center.y);

            angle += 2 * Math.PI / this.sides;
        }

        // Complete the last triangle
        vertices.add(vertices.get(0));
        vertices.add(vertices.get(1));

        // Convert List<Float> to float[]
        float[] verticesArr = new float[vertices.size()];

        for (int i = 0; i < verticesArr.length; i++) {
            verticesArr[i] = vertices.get(i);
        }

        return new Model(verticesArr);
    }

    public void draw() {
        this.model.render();
    }

    @Override
    public void close() {
        this.model.close();
    }
}
