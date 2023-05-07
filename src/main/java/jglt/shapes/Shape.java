package jglt.shapes;

import jglt.coords.PixelCoords;
import jglt.coords.ScreenCoords;
import jglt.graphics.Model;

public abstract class Shape {
    protected Model model;
    /** The angle of the shape from the x-axis in radians */
    protected double axisAngle;

    public abstract void draw();
    public abstract void shift(float x, float y);
    public abstract float[] getVertices();
    public abstract ScreenCoords getCenter();

    public double getAxisAngle() {
        return this.axisAngle;
    }

    public void setAxisAngle(double angleRadians) {
        double delta = angleRadians - this.axisAngle;
        this.rotateAxis(delta);
    }

    public abstract boolean collidesWith(Rect rect);
    public abstract boolean collidesWith(Circle circle);

    /**
     * Modifies the vertices passed to rotate across the origin
     *
     * @param vertices The vertex data to rotate.
     * @param angleRadians The angle, in radians, to rotate
     * @return the vertices object that was passed in
     */
    protected static float[] rotateAxis(float[] vertices, double angleRadians) {
        angleRadians *= -1;  // make the shape rotate clockwise when angleRadians > 0

        for (int i = 0; i < vertices.length; i += 2) {
            float x = ScreenCoords.distXtoPixelCoords(vertices[i]);

            // this will not cause an out-of-bounds error since vertices.length is required to be even
            // to be drawn to the screen correctly
            float y = ScreenCoords.distYtoPixelCoords(vertices[i + 1]);

            double theta = Math.atan2(y, x);
            double hyp = Math.sqrt(x * x + y * y);
            double newTheta = 0.5 * Math.PI - theta - angleRadians;

            // Set the vertices to the new vertices
            vertices[i] = PixelCoords.distXToScreenDist((float) Math.round(Math.sin(newTheta) * hyp * 10000000) / 10000000);      // x
            vertices[i + 1] = PixelCoords.distYToScreenDist((float) Math.round(Math.cos(newTheta) * hyp * 10000000) / 10000000);  // y
        }

        return vertices;
    }

    public void rotateAxis(double angleRadians) {
        float[] vertices = this.getVertices();
        Shape.rotateAxis(vertices, angleRadians);
        this.axisAngle += angleRadians;

        this.model.changeVertices(vertices);
    }
}
