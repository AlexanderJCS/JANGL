package jglt.shapes;

import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.Model;

import java.util.ArrayList;
import java.util.List;

public class Circle implements Shape, AutoCloseable {
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

    @Override
    public void shift(float x, float y) {
        this.center.x += x;
        this.center.y += y;

        this.model = this.toModel();
    }

    public ScreenCoords getCenter() {
        return this.center;
    }

    public int getSides() {
        return this.sides;
    }

    public float getRadiusX() {
        return this.radiusX;
    }

    public float getRadiusY() {
        return this.radiusY;
    }

    private Model toModel() {
        List<Float> vertices = new ArrayList<>();
        double angle = 0;  // radians

        // Add the origin
        vertices.add(this.getCenter().x);
        vertices.add(this.getCenter().y);

        // Add the edges
        while (angle < 2 * Math.PI) {
            float x = (float) (this.radiusX * Math.sin(angle));
            float y = (float) (this.radiusY * Math.cos(angle));

            float offsetX = x + this.center.x;
            float offsetY = y + this.center.y;

            vertices.add(offsetX);
            vertices.add(offsetY);

            angle += 2 * Math.PI / this.sides;
        }

        List<Integer> indices = new ArrayList<>();
        for (int i = 1; i < vertices.size() / 2; i++) {
            indices.add(i);
            indices.add(0);
            indices.add(i + 1 == vertices.size() ? 1 : i + 1);  // if i + 1 == vertices.size() go back to index 1
        }

        float[] verticesArr = new float[vertices.size()];
        for (int i = 0; i < verticesArr.length; i++) {
            verticesArr[i] = vertices.get(i);
        }

        int[] indicesArr = new int[indices.size()];
        for (int i = 0; i < indicesArr.length; i++) {
            indicesArr[i] = indices.get(i);
        }

        return new Model(verticesArr, indicesArr);
    }

    @Override
    public void draw() {
        this.model.render();
    }

    @Override
    public void close() {
        this.model.close();
    }

    /**
     * Returns true if one of the rectangle's vertices collides with the circle. This needs to be fixed
     * in the future.
     * @param other The rectangle to test collision with.
     * @return If one of the rectangle's vertices collides with the circle
     */
    @Override
    public boolean collidesWith(Rect other) {
        // https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection

        PixelCoords circle = this.getCenter().toPixelCoords();
        PixelCoords rect = other.getCenter().toPixelCoords();

        float rectWidth = ScreenCoords.distXtoPixelCoords(other.width);
        float rectHeight = ScreenCoords.distYtoPixelCoords(other.height);

        PixelCoords circleDistance = new PixelCoords(0, 0);

        circleDistance.x = Math.abs(circle.x - rect.x);
        circleDistance.y = Math.abs(circle.y - rect.y);

        if (circleDistance.x > (rectWidth/2 + this.radiusX)) { return false; }
        if (circleDistance.y > (rectHeight/2 + this.radiusX)) { return false; }

        if (circleDistance.x <= (rectWidth/2)) { return true; }
        if (circleDistance.y <= (rectHeight/2)) { return true; }

        double cornerDistanceSq = Math.pow(circleDistance.x - rectWidth/2, 2) +
                Math.pow(circleDistance.y - rectHeight/2, 2);

        return (cornerDistanceSq <= Math.pow(this.getRadiusX(), 2));
    }

    /**
     * Checks if a circle collides with another circle.
     * @param other The other circle.
     * @return Whether they collide.
     */
    @Override
    public boolean collidesWith(Circle other) {
        float combinedRadius = this.getRadiusX() + other.getRadiusX();
        double dist = this.getCenter().toPixelCoords().dist(other.getCenter().toPixelCoords());

        return dist <= combinedRadius;
    }
}
