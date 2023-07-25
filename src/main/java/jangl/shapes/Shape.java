package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.Bindable;
import jangl.graphics.Camera;
import jangl.graphics.models.Model;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.VertexShader;
import jangl.graphics.shaders.premade.DefaultVertShader;
import jangl.util.ArrayUtils;
import jangl.util.Range;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Shape implements AutoCloseable {
    private static final ShaderProgram defaultShader = new ShaderProgram(new DefaultVertShader());
    protected final Transform transform;
    protected Model model;

    public Shape() {
        this.transform = new Transform();
    }

    public static boolean collides(Shape shape1, Shape shape2) {
        Vector2f[] s1Vertices = ArrayUtils.toVector2fArray(shape1.getExteriorVertices());
        Vector2f[] s2Vertices = ArrayUtils.toVector2fArray(shape2.getExteriorVertices());

        // Optimization that can have HUGE performance improvements when the two objects are not near each other
        // Act like the two objects are perfect spheres, where their radii are the farthest point from the center of
        // the object. If the two spheres are not colliding, there's no way that the two objects can be colliding
        // using more sophisticated collision detection methods.
        Vector2f s1CenterVec = shape1.getTransform().getCenter().toVector2f();
        Vector2f s2CenterVec = shape2.getTransform().getCenter().toVector2f();

        Vector2f s1FarthestPoint = ArrayUtils.getFarthestPointFrom(s1Vertices, s1CenterVec);
        Vector2f s2FarthestPoint = ArrayUtils.getFarthestPointFrom(s2Vertices, s2CenterVec);

        float s1Radius = s1FarthestPoint.distance(s1CenterVec);
        float s2Radius = s2FarthestPoint.distance(s2CenterVec);

        if (!collides(shape1.getTransform().getCenter(), s1Radius, shape2.getTransform().getCenter(), s2Radius)) {
            return false;
        }

        Vector2f[] s1Axes = shape1.getOutsideVectors();
        Vector2f[] s2Axes = shape2.getOutsideVectors();

        List<Vector2f> combined = new ArrayList<>(s1Axes.length + s2Axes.length);
        Collections.addAll(combined, s1Axes);
        Collections.addAll(combined, s2Axes);


        for (Vector2f axis : combined) {
            axis.perpendicular();

            Range s1Range = projectShapeOntoAxis(s1Vertices, axis);
            Range s2Range = projectShapeOntoAxis(s2Vertices, axis);

            // If the ranges do not intersect, the shapes are not colliding
            if (!s1Range.intersects(s2Range)) {
                return false;
            }
        }

        return true;
    }

    public static boolean collides(Shape shape, WorldCoords point) {
        Vector2f pointVector = point.toVector2f();
        Vector2f[] s1Axes = shape.getOutsideVectors();
        Vector2f[] s1Vertices = ArrayUtils.toVector2fArray(shape.getExteriorVertices());

        for (Vector2f axis : s1Axes) {
            axis.perpendicular();

            Range s1Range = projectShapeOntoAxis(s1Vertices, axis);
            float projectedPoint = pointVector.dot(axis);

            // If the ranges do not intersect, the shapes are not colliding
            if (!s1Range.intersects(projectedPoint)) {
                return false;
            }
        }

        return true;
    }

    public static boolean collides(Shape shape, Circle circle) {
        Vector2f circleCenterVector = circle.getTransform().getCenter().toVector2f();
        Vector2f[] s1Axes = shape.getOutsideVectors();

        List<Vector2f> combined = new ArrayList<>(Arrays.stream(s1Axes).toList());
        // The perpendicular axis between the shape's center and the circle's center
        combined.add(shape.getTransform().getCenter().toVector2f().sub(circleCenterVector).normalize().perpendicular());

        Vector2f[] s1Vertices = ArrayUtils.toVector2fArray(shape.getExteriorVertices());

        for (Vector2f axis : combined) {
            axis.perpendicular();

            Range s1Range = projectShapeOntoAxis(s1Vertices, axis);
            float projectedCenter = circleCenterVector.dot(axis);
            Range circleRange = new Range(projectedCenter - circle.getRadius(), projectedCenter + circle.getRadius());

            // If the ranges do not intersect, the shapes are not colliding
            if (!s1Range.intersects(circleRange)) {
                return false;
            }
        }

        return true;
    }

    public static boolean collides(Circle circle, Shape shape) {
        return collides(shape, circle);
    }

    private static Range projectShapeOntoAxis(Vector2f[] vertices, Vector2f axis) {
        float[] dotProducts = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            dotProducts[i] = vertices[i].dot(axis);
        }

        return new Range(ArrayUtils.getMin(dotProducts), ArrayUtils.getMax(dotProducts));
    }

    /**
     * Checks if two circles collide.
     * @param circle1 The first circle.
     * @param circle2 The second circle.
     * @return If they collide.
     */
    public static boolean collides(Circle circle1, Circle circle2) {
        return collides(
                circle1.getTransform().getCenter(), circle1.getRadius(),
                circle2.getTransform().getCenter(), circle2.getRadius()
        );
    }

    /**
     * Checks collision between two points with a designated radius. This is used for culling objects out of screen
     * and for optimizations with collision. If you want to check collision between two circles,
     * use Shape.collides(Circle, Circle).
     *
     * @param point1  The middle coordinate of the first shape
     * @param radius1 The radius of the first shape.
     * @param point2  The middle coordinate of the second shape.
     * @param radius2 The radius of the second shape.
     * @return If they collide.
     */
    public static boolean collides(WorldCoords point1, float radius1, WorldCoords point2, float radius2) {
        double distSquared = Math.pow(point1.x - point2.x, 2) +
                Math.pow(point1.y - point2.y, 2);

        double combinedRadiiSquared = Math.pow(radius1 + radius2, 2);

        return distSquared <= combinedRadiiSquared;
    }

    public static boolean collides(Circle circle, WorldCoords point) {
        WorldCoords circleCenter = circle.getTransform().getCenter();

        double radiusPixelsSquared = Math.pow(WorldCoords.distToPixelCoords(circle.getRadius()), 2);
        double distSquared = Math.pow(circleCenter.x - point.x, 2) + Math.pow(circleCenter.y - point.y, 2);

        return distSquared <= radiusPixelsSquared;
    }

    public static boolean collides(WorldCoords point, Circle circle) {
        return collides(circle, point);
    }

    protected boolean shouldDraw() {
        // Check if the shape is off-screen. If it is, then it's best not to waste a draw call on it
        Vector2f farthestCoordinate = ArrayUtils.getFarthestPointFrom(
                ArrayUtils.toVector2fArray(this.getExteriorVertices()),
                this.getTransform().getCenter().toVector2f()
        );

        float radius = farthestCoordinate.distance(this.getTransform().getCenter().toVector2f());
        float windowRadius = (float) (Math.pow(WorldCoords.getMiddle().x, 2) + Math.pow(WorldCoords.getMiddle().y, 2));

        return collides(Camera.getCenter(), windowRadius, this.getTransform().getCenter(), radius);
    }

    public void draw() {
        // Proceed with drawing the shape
        if (ShaderProgram.getBoundProgram() == null) {
            defaultShader.bind();
        }

        ShaderProgram boundProgram = ShaderProgram.getBoundProgram();
        VertexShader vertexShader = boundProgram.getVertexShader();

        vertexShader.setMatrixUniforms(
                boundProgram.getProgramID(),
                this.transform.getTransformMatrix(),
                this.transform.getRotationMatrix()
        );
    }

    public void draw(Bindable bindable) {
        bindable.bind();
        this.draw();
        bindable.unbind();
    }

    public Transform getTransform() {
        return transform;
    }

    public abstract float[] calculateVertices();

    /**
     * Calculates the vertices with the matrix applied. This method is not recommended to be called much since matrix
     * multiplication on the CPU is slow.
     *
     * @return The vertices in normalize device coords. Odd indices are x coords and even indices are y coords.
     */
    public float[] calculateVerticesMatrix() {
        float[] verticesNoMatrix = this.calculateVertices();
        float[] verticesMatrix = new float[verticesNoMatrix.length];

        Matrix4f matrix = this.transform.getMatrix();

        for (int i = 0; i < verticesNoMatrix.length; i += 2) {
            Vector4f vertex = new Vector4f(verticesNoMatrix[i], verticesNoMatrix[i + 1], 0, 1);
            vertex.mul(matrix);
            verticesMatrix[i] = vertex.x;
            verticesMatrix[i + 1] = vertex.y;
        }

        return verticesMatrix;
    }

    /**
     * @return All vertices on the exterior of the shape. Returns the vertices in such an order where
     * if you were to connect index 0 to index 1, 1 to 2, the last index to index 0, etc., it would
     * form a line of the outside.
     */
    public abstract float[] getExteriorVertices();

    public Vector2f[] getOutsideVectors() {
        Vector2f[] exteriorVertices = ArrayUtils.toVector2fArray(this.getExteriorVertices());
        Vector2f[] outsideVectors = new Vector2f[exteriorVertices.length];

        for (int i = 0; i < exteriorVertices.length; i++) {
            Vector2f lastPoint;
            if (i == 0) {
                lastPoint = exteriorVertices[exteriorVertices.length - 1];
            } else {
                lastPoint = exteriorVertices[i - 1];
            }

            outsideVectors[i] = new Vector2f(exteriorVertices[i]).sub(lastPoint).normalize();
        }

        return outsideVectors;
    }
}
