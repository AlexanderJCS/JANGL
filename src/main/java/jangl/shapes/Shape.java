package jangl.shapes;

import jangl.coords.PixelCoords;
import jangl.coords.NDCoords;
import jangl.graphics.Texture;
import jangl.graphics.models.Model;
import jangl.graphics.shaders.ShaderProgram;
import jangl.util.ArrayUtils;
import jangl.util.Range;

import java.util.HashSet;

public abstract class Shape implements AutoCloseable {
    protected Model model;
    /**
     * The angle of the shape from the x-axis in radians
     */
    protected double axisAngle;
    protected double localAngle;

    public Shape() {
        this.axisAngle = 0;
        this.localAngle = 0;
    }

    /**
     * Modifies the vertices passed to rotate across the origin (the center of the screen). This is primarily used for collision.
     * NOTE: the vertices must be in the units of NDCoords to work properly.
     *
     * @param vertices     The vertex data to rotate. Even indices are x coordinates, odd indices are y coordinates.
     * @param angleRadians The angle, in radians, to rotate
     * @return the vertices object that was passed in
     */
    public static float[] rotateAxis(float[] vertices, double angleRadians) {
        angleRadians *= -1;  // make the shape rotate clockwise when angleRadians > 0

        for (int i = 0; i < vertices.length; i += 2) {
            float x = NDCoords.distXtoPixelCoords(vertices[i]);

            // this will not cause an out-of-bounds error since vertices.length is required to be even
            // to be drawn to the screen correctly
            float y = NDCoords.distYtoPixelCoords(vertices[i + 1]);

            double theta = Math.atan2(y, x);
            double hyp = Math.sqrt(x * x + y * y);
            double newTheta = 0.5 * Math.PI - theta - angleRadians;

            // Set the vertices to the new vertices
            vertices[i] = PixelCoords.distXtoNDC((float) Math.round(Math.sin(newTheta) * hyp * 10000000) / 10000000);      // x
            vertices[i + 1] = PixelCoords.distYtoNDC((float) Math.round(Math.cos(newTheta) * hyp * 10000000) / 10000000);  // y
        }

        return vertices;
    }

    public static boolean collides(Shape shape1, Shape shape2) {
        double[] angles = shape1.getOutsideEdgeAngles();
        double[] otherAngles = shape2.getOutsideEdgeAngles();

        HashSet<Double> anglesSet = new HashSet<>();

        for (double angle : angles) {
            double nonNegativeAngle = angle > 0 ? angle : angle + Math.PI;
            anglesSet.add(Math.round(nonNegativeAngle * 10000000000d) / 10000000000d);
        }

        for (double angle : otherAngles) {
            double nonNegativeAngle = angle > 0 ? angle : angle + Math.PI;
            anglesSet.add(Math.round(nonNegativeAngle * 10000000000d) / 10000000000d);
        }

        double s1BeginningAngle = shape1.getAxisAngle();
        double s2BeginningAngle = shape2.getAxisAngle();

        double prevAngle = 0;
        for (double angle : anglesSet) {
            // Allows the shape to be rotated once per iteration instead of twice
            // Giving a 20% - 25% performance increase
            double delta = angle - prevAngle;
            prevAngle = angle;

            // Rotate the axis of the two shapes so that one side is flat
            // This is used as a substitute for projection
            float[] s1Vertices = shape1.rotateAxis(delta);
            float[] s2Vertices = shape2.rotateAxis(delta);

            float[] s1verticesX = ArrayUtils.getEven(s1Vertices);
            float[] s1verticesY = ArrayUtils.getOdd(s1Vertices);

            float[] s2verticesX = ArrayUtils.getEven(s2Vertices);
            float[] s2verticesY = ArrayUtils.getOdd(s2Vertices);

            Range s1RangeX = new Range(ArrayUtils.getMin(s1verticesX), ArrayUtils.getMax(s1verticesX));
            Range s1RangeY = new Range(ArrayUtils.getMin(s1verticesY), ArrayUtils.getMax(s1verticesY));

            Range s2RangeX = new Range(ArrayUtils.getMin(s2verticesX), ArrayUtils.getMax(s2verticesX));
            Range s2RangeY = new Range(ArrayUtils.getMin(s2verticesY), ArrayUtils.getMax(s2verticesY));

            // If the ranges do not intersect, the shapes are not colliding
            if (!s1RangeX.intersects(s2RangeX) || !s1RangeY.intersects(s2RangeY)) {
                // Rotate the axis angles back to what they were at the beginning
                shape1.setAxisAngle(s1BeginningAngle);
                shape2.setAxisAngle(s2BeginningAngle);

                return false;
            }
        }

        // Rotate the axis angles back to what they were at the beginning
        shape1.setAxisAngle(s1BeginningAngle);
        shape2.setAxisAngle(s2BeginningAngle);

        return true;
    }

    public static boolean collides(Shape polygon, Circle circle) {
        double[] angles = polygon.getOutsideEdgeAngles();

        double beginningAngle = polygon.getAxisAngle();

        for (int i = 0; i < angles.length; i++) {
            double delta;

            if (i == 0) {
                delta = angles[i];
            } else {
                delta = angles[i] - angles[i - 1];
            }

            // Rotate the axis of the two shapes so that one side is flat
            // This is used as a substitute for projection
            float[] polyVertices = polygon.rotateAxis(delta);
            float[] circleCenter = Shape.rotateAxis(
                    new float[]{circle.getCenter().x, circle.getCenter().y},
                    angles[i]
            );

            float[] s1verticesX = ArrayUtils.getEven(polyVertices);
            float[] s1verticesY = ArrayUtils.getOdd(polyVertices);

            // Here, "s" means "shape". So "s1Range" means shape 1 range.
            Range s1RangeX = new Range(ArrayUtils.getMin(s1verticesX), ArrayUtils.getMax(s1verticesX));
            Range s1RangeY = new Range(ArrayUtils.getMin(s1verticesY), ArrayUtils.getMax(s1verticesY));

            Range s2RangeX = new Range(circleCenter[0] - circle.getRadiusX(), circleCenter[0] + circle.getRadiusX());
            Range s2RangeY = new Range(circleCenter[1] - circle.getRadiusY(), circleCenter[1] + circle.getRadiusY());

            // If the ranges do not intersect, the shapes are not colliding
            if (!s1RangeX.intersects(s2RangeX) || !s1RangeY.intersects(s2RangeY)) {
                // Rotate the axis angles back to what they were at the beginning
                polygon.setAxisAngle(beginningAngle);

                return false;
            }
        }

        // Rotate the axis angles back to what they were at the beginning
        polygon.setAxisAngle(beginningAngle);

        return true;
    }

    public static boolean collides(Circle circle, Shape polygon) {
        return collides(polygon, circle);
    }

    public static boolean collides(Circle circle1, Circle circle2) {
        PixelCoords circle1Center = circle1.getCenter().toPixelCoords();
        PixelCoords circle2Center = circle2.getCenter().toPixelCoords();

        double distSquared = Math.pow(circle1Center.x - circle2Center.x, 2) +
                Math.pow(circle1Center.y - circle2Center.y, 2);

        double combinedRadiiSquared = Math.pow(
                NDCoords.distXtoPixelCoords(circle1.getRadiusX()) +
                        NDCoords.distXtoPixelCoords(circle2.getRadiusX()),
                2
        );

        return distSquared <= combinedRadiiSquared;
    }

    public static boolean collides(Circle circle, NDCoords point) {
        PixelCoords circleCenter = circle.getCenter().toPixelCoords();
        PixelCoords pointPixels = point.toPixelCoords();
        double radiusPixelsSquared = Math.pow(NDCoords.distXtoPixelCoords(circle.getRadiusX()), 2);

        double distSquared = Math.pow(circleCenter.x - pointPixels.x, 2) +
                Math.pow(circleCenter.y - pointPixels.y, 2);

        return distSquared <= radiusPixelsSquared;
    }

    public static boolean collides(NDCoords point, Circle circle) {
        return collides(circle, point);
    }

    public static boolean collides(Shape polygon, NDCoords point) {
        double[] angles = polygon.getOutsideEdgeAngles();

        double beginningAngle = polygon.getAxisAngle();

        float[] pointCoordsArr = new float[]{point.x, point.y};
        for (int i = 0; i < angles.length; i++) {
            double delta;

            if (i == 0) {
                delta = angles[i];
            } else {
                delta = angles[i] - angles[i - 1];
            }

            // Rotate the axis of the two shapes so that one side is flat
            // This is used as a substitute for projection
            float[] polyVertices = polygon.rotateAxis(delta);
            Shape.rotateAxis(pointCoordsArr, delta);

            float[] s1verticesX = ArrayUtils.getEven(polyVertices);
            float[] s1verticesY = ArrayUtils.getOdd(polyVertices);

            // Here, "s" means "shape". So "s1Range" means shape 1 range.
            Range s1RangeX = new Range(ArrayUtils.getMin(s1verticesX), ArrayUtils.getMax(s1verticesX));
            Range s1RangeY = new Range(ArrayUtils.getMin(s1verticesY), ArrayUtils.getMax(s1verticesY));

            // If the ranges do not intersect, the shapes are not colliding
            if (!s1RangeX.intersects(pointCoordsArr[0]) || !s1RangeY.intersects(pointCoordsArr[1])) {
                // Rotate the axis angles back to what they were at the beginning
                polygon.setAxisAngle(beginningAngle);

                return false;
            }
        }

        // Rotate the axis angles back to what they were at the beginning
        polygon.setAxisAngle(beginningAngle);

        return true;
    }

    protected static float[] rotateLocal(float[] vertices, NDCoords center, double angle) {
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

    public abstract void draw();

    public void draw(ShaderProgram shader) {
        shader.bind();
        this.draw();
        ShaderProgram.unbind();
    }

    public void draw(Texture texture) {
        texture.bind();
        this.draw();
        Texture.unbind();
    }

    public abstract void shift(float x, float y);

    public abstract float[] calculateVertices();

    public abstract NDCoords getCenter();

    public void setCenter(NDCoords newCenter) {
        NDCoords currentCenter = this.getCenter();
        this.shift(newCenter.x - currentCenter.x, newCenter.y - currentCenter.y);
    }

    /**
     * @return All vertices on the exterior of the shape. Returns the vertices in such an order where
     * if you were to connect index 0 to index 1, 1 to 2, the last index to index 0, etc., it would
     * form a line of the outside.
     */
    public abstract float[] getExteriorVertices();

    /**
     * Calculates the angle (theta) between the x plane and every outside line.
     * Used for the Separating Axis Theorem (SAT) collision detection algorithm.
     *
     * @return an array of all the thetas between the x plane and every outside line in radians
     */
    public double[] getOutsideEdgeAngles() {
        float[] exteriorVertices = this.getExteriorVertices();
        double[] outsideAngles = new double[exteriorVertices.length / 2];

        for (int i = 0; i < outsideAngles.length; i++) {
            float deltaX, deltaY;

            // Avoid an out-of-bounds error
            if (i * 2 + 3 >= exteriorVertices.length) {
                deltaX = exteriorVertices[i * 2] - exteriorVertices[0];
                deltaY = exteriorVertices[i * 2 + 1] - exteriorVertices[1];
            } else {
                deltaX = exteriorVertices[i * 2] - exteriorVertices[i * 2 + 2];
                deltaY = exteriorVertices[i * 2 + 1] - exteriorVertices[i * 2 + 3];
            }

            outsideAngles[i] = Math.atan2(NDCoords.distYtoPixelCoords(deltaY), NDCoords.distXtoPixelCoords(deltaX));
        }

        return outsideAngles;
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
     *
     * @param angleRadians The angle to rotate the axis in radians.
     */
    public float[] rotateAxis(double angleRadians) {
        this.axisAngle += angleRadians;
        float[] vertices = this.calculateVertices();

        this.model.subVertices(vertices, 0);

        return vertices;
    }

    /**
     * Rotate the object while keeping the center of the object the same. This differs from rotateAxis, which
     * will rotate the object across the center of the screen.
     *
     * @param angle The angle, in radians, to rotate the shape by
     */
    public void rotateLocal(double angle) {
        NDCoords originalPosition = this.getCenter();
        this.setCenter(new NDCoords(0, 0));

        this.rotateAxis(angle);
        this.axisAngle -= angle;  // undo the change that rotateAxis did

        this.setCenter(originalPosition);
        this.localAngle += angle;
    }
}
