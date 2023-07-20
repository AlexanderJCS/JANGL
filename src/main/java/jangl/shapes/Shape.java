package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.Bindable;
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
        Vector2f[] s1Axes = shape1.getOutsideVectors();
        Vector2f[] s2Axes = shape2.getOutsideVectors();

        List<Vector2f> combined = new ArrayList<>(s1Axes.length + s2Axes.length);
        Collections.addAll(combined, s1Axes);
        Collections.addAll(combined, s2Axes);

        Vector2f[] s1Vertices = ArrayUtils.toVector2fArray(shape1.getExteriorVertices());
        Vector2f[] s2Vertices = ArrayUtils.toVector2fArray(shape2.getExteriorVertices());

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

    private static Range projectShapeOntoAxis(Vector2f[] vertices, Vector2f axis) {
        float[] dotProducts = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            dotProducts[i] = vertices[i].dot(axis);
        }

        return new Range(ArrayUtils.getMin(dotProducts), ArrayUtils.getMax(dotProducts));
    }

    public static boolean collides(Circle circle1, Circle circle2) {
        WorldCoords circle1Center = circle1.getTransform().getCenter();
        WorldCoords circle2Center = circle2.getTransform().getCenter();

        double distSquared = Math.pow(circle1Center.x - circle2Center.x, 2) +
                Math.pow(circle1Center.y - circle2Center.y, 2);

        double combinedRadiiSquared = Math.pow(circle1.getRadius() + circle2.getRadius(), 2);

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

    public void draw() {
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
