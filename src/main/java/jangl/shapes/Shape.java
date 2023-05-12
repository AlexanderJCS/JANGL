package jangl.shapes;

import jangl.graphics.shaders.Shader;
import jangl.util.ArrayUtils;
import jangl.coords.PixelCoords;
import jangl.util.Range;
import jangl.coords.ScreenCoords;
import jangl.graphics.models.Model;

import java.util.Arrays;
import java.util.List;

public abstract class Shape implements AutoCloseable {
    protected Model model;
    /** The angle of the shape from the x-axis in radians */
    protected double axisAngle;
    protected double localAngle;

    public Shape() {
        this.axisAngle = 0;
        this.localAngle = 0;
    }

    public abstract void draw();

    public void draw(Shader shader) {
        shader.bind();
        this.draw();
        shader.unbind();
    }

    public abstract void shift(float x, float y);
    public abstract float[] getVertices();
    public abstract ScreenCoords getCenter();

    public void setCenter(ScreenCoords newCenter) {
        ScreenCoords currentCenter = this.getCenter();
        this.shift(newCenter.x - currentCenter.x, newCenter.y - currentCenter.y);
    }

    /**
     * @return All vertices on the exterior of the shape. Returns the vertices in such an order where
     *         if you were to connect index 0 to index 1, 1 to 2, the last index to index 0, etc., it would
     *         form a line of the outside.
     */
    public abstract float[] getExteriorVertices();


    /**
     * Calculates the angle (theta) between the x plane and every outside line.
     * Used for the Separating Axis Theorem (SAT) collision detection algorithm.
     * @return an array of all the thetas between the x plane and every outside line in radians
     */
    public double[] getOutsideEdgeAngles() {
        float[] exteriorVertices = this.getExteriorVertices();
        double[] normalAngles = new double[exteriorVertices.length / 2];

        for (int i = 0; i < normalAngles.length; i++) {
            float deltaX, deltaY;

            // Avoid an out-of-bounds error
            if (i * 2 + 3 >= exteriorVertices.length) {
                deltaX = exteriorVertices[i * 2] - exteriorVertices[0];
                deltaY = exteriorVertices[i * 2 + 1] - exteriorVertices[1];
            } else {
                deltaX = exteriorVertices[i * 2] - exteriorVertices[i * 2 + 2];
                deltaY = exteriorVertices[i * 2 + 1] - exteriorVertices[i * 2 + 3];
            }

            normalAngles[i] = Math.atan2(ScreenCoords.distYtoPixelCoords(deltaY), ScreenCoords.distXtoPixelCoords(deltaX));
        }

        return normalAngles;
    }

    /**
     * Modifies the vertices passed to rotate across the origin (the center of the screen). This is used for collision.
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
            vertices[i] = PixelCoords.distXtoScreenDist((float) Math.round(Math.sin(newTheta) * hyp * 10000000) / 10000000);      // x
            vertices[i + 1] = PixelCoords.distYtoScreenDist((float) Math.round(Math.cos(newTheta) * hyp * 10000000) / 10000000);  // y
        }

        return vertices;
    }

    /**
     * @return An array of all the X vertices. Used for collision.
     */
    public float[] getXVertices() {
        float[] vertices = this.getVertices();
        float[] xVertices = new float[vertices.length / 2];

        for (int i = 0; i < xVertices.length; i++) {
            xVertices[i] = vertices[i * 2];
        }

        return xVertices;
    }

    /**
     * @return An array of all the y vertices. Used for collision.
     */
    public float[] getYVertices() {
        float[] vertices = this.getVertices();
        float[] yVertices = new float[vertices.length / 2];

        for (int i = 0; i < yVertices.length; i++) {
            yVertices[i] = vertices[i * 2 + 1];
        }

        return yVertices;
    }

    /**
     * Uses the Separating Axis Theorem (SAT) collision detection method.
     *
     * @param other The other shape to check collision with
     * @return True if the objects collide, false otherwise
     */
    public boolean collidesWith(Shape other) {
        double[] angles = this.getOutsideEdgeAngles();
        double[] otherAngles = other.getOutsideEdgeAngles();

        // https://stackoverflow.com/questions/754294/convert-an-array-of-primitive-longs-into-a-list-of-longs
        // modified according to IntelliJ's recommendation
        List<Double> anglesList = new java.util.ArrayList<>(Arrays.stream(angles).boxed().toList());
        anglesList.addAll(Arrays.stream(otherAngles).boxed().toList());

        for (double angle : anglesList) {
            this.rotateAxis(angle);
            other.rotateAxis(angle);

            float[] thisXVerts = this.getXVertices();
            float[] thisYVerts = this.getYVertices();

            float[] otherXVerts = other.getXVertices();
            float[] otherYVerts = other.getYVertices();

            Range thisXRange = new Range(ArrayUtils.getMin(thisXVerts), ArrayUtils.getMax(thisXVerts));
            Range thisYRange = new Range(ArrayUtils.getMin(thisYVerts), ArrayUtils.getMax(thisYVerts));

            Range otherXRange = new Range(ArrayUtils.getMin(otherXVerts), ArrayUtils.getMax(otherXVerts));
            Range otherYRange = new Range(ArrayUtils.getMin(otherYVerts), ArrayUtils.getMax(otherYVerts));

            if (!thisXRange.intersects(otherXRange) || !thisYRange.intersects(otherYRange)) {
                this.rotateAxis(-angle);
                other.rotateAxis(-angle);

                return false;
            }

            this.rotateAxis(-angle);
            other.rotateAxis(-angle);
        }

        return true;
    }

    public double getAxisAngle() {
        return this.axisAngle;
    }

    public void setAxisAngle(double angleRadians) {
        double delta = angleRadians - this.getAxisAngle();
        this.rotateAxis(delta);
    }

    public double getLocalAngle() {
        return this.localAngle;
    }

    public void setLocalAngle(double angleRadians) {
        double delta = angleRadians - this.getLocalAngle();
        this.rotateLocal(delta);
    }

    /**
     * Rotate the axis by a certain amount across the origin (center of the screen).
     * @param angleRadians The angle to rotate the axis in radians.
     */
    public void rotateAxis(double angleRadians) {
        float[] vertices = this.getVertices();
        Shape.rotateAxis(vertices, angleRadians);
        this.axisAngle += angleRadians;

        this.model.changeVertices(vertices);
    }

    protected static float[] rotateLocal(float[] vertices, ScreenCoords center, double angle) {
        // Shift the x vertices
        for (int i = 0; i < vertices.length; i += 2) {
            vertices[i] -= center.x;
        }

        // Shift the y vertices
        for (int i = 1; i < vertices.length; i += 2) {
            vertices[i] -= center.y;
        }

        Shape.rotateAxis(vertices, angle);

        // Un-shift the x vertices
        for (int i = 0; i < vertices.length; i += 2) {
            vertices[i] += center.x;
        }

        // Un-shift the y vertices
        for (int i = 1; i < vertices.length; i += 2) {
            vertices[i] += center.y;
        }

        return vertices;
    }

    /**
     * Rotate the object while keeping the center of the object the same. This differs from rotateAxis, which
     * will rotate the object across the center of the screen.
     *
     * @param angle The angle, in radians, to rotate the shape by
     */
    public void rotateLocal(double angle) {
        ScreenCoords originalPosition = this.getCenter();
        this.setCenter(new ScreenCoords(0, 0));

        this.rotateAxis(angle);
        this.axisAngle -= angle;  // undo the change that rotateAxis did

        this.setCenter(originalPosition);
        this.localAngle += angle;
    }
}
