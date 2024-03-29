package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.Bindable;
import jangl.graphics.Camera;
import jangl.graphics.models.Model;
import jangl.graphics.models.TexturedModel;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.VertexShader;
import jangl.graphics.shaders.premade.DefaultVertShader;
import jangl.io.mouse.Mouse;
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

    private float texRepeatX, texRepeatY;

    public Shape() {
        this.transform = new Transform();

        this.setTexRepeatX(1);
        this.setTexRepeatY(1);
    }

    /**
     * Checks if two shapes collide.
     *
     * @param shape1 The first shape.
     * @param shape2 The second shape.
     * @return If they collide.
     */
    public static boolean collides(Shape shape1, Shape shape2) {
        // Optimization that can have huge performance improvements when the two objects are not near each other:
        // Act like the two objects are perfect spheres, where their radii are the farthest point from the center of
        // the object. If the two spheres are not colliding, there's no way that the two objects can be colliding
        // using more sophisticated collision detection methods.
        Vector2f[] s1VerticesNoMatrix = ArrayUtils.toVector2fArray(shape1.calculateVertices());
        Vector2f[] s2VerticesNoMatrix = ArrayUtils.toVector2fArray(shape2.calculateVertices());

        Vector2f s1FarthestPoint = ArrayUtils.getFarthestPointFrom(s1VerticesNoMatrix, new Vector2f().zero()).mul(shape1.getTransform().getScale());
        Vector2f s2FarthestPoint = ArrayUtils.getFarthestPointFrom(s2VerticesNoMatrix, new Vector2f().zero());

        float s1Radius = s1FarthestPoint.distance(new Vector2f().zero()) * Math.max(shape1.getTransform().getScaleX(), shape1.getTransform().getScaleY());
        float s2Radius = s2FarthestPoint.distance(new Vector2f().zero()) * Math.max(shape2.getTransform().getScaleX(), shape2.getTransform().getScaleY());

        if (!collides(shape1.getTransform().getCenter(), s1Radius, shape2.getTransform().getCenter(), s2Radius)) {
            return false;
        }

        // Proceed with normal collision detection
        Vector2f[] s1Vertices = ArrayUtils.toVector2fArray(shape1.calculateVerticesMatrix());
        Vector2f[] s2Vertices = ArrayUtils.toVector2fArray(shape2.calculateVerticesMatrix());

        Vector2f[] s1Axes = Shape.getOutsideVectors(s1Vertices);
        Vector2f[] s2Axes = Shape.getOutsideVectors(s2Vertices);

        List<Vector2f> combined = new ArrayList<>(s1Axes.length + s2Axes.length);
        Collections.addAll(combined, s1Axes);
        Collections.addAll(combined, s2Axes);

        for (Vector2f axis : combined) {
            axis.perpendicular();

            Range s1Range = Shape.projectShapeOntoAxis(s1Vertices, axis);
            Range s2Range = Shape.projectShapeOntoAxis(s2Vertices, axis);

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

    /**
     * Checks for collision between a shape and a circle. If the circle object is transformed into an ellipse, this
     * method will not work correctly, instead assuming that the radius of the circle is equal to the ellipsis's X
     * radius. If you want to check for collision between a shape and an ellipse, cast the ellipse to a shape and
     * use the Shape.collides(Shape, Shape) method.
     *
     * @param shape The shape object.
     * @param circle The circle object.
     * @return If they collide.
     */
    public static boolean collides(Shape shape, Circle circle) {
        Vector2f circleCenterVector = circle.getTransform().getCenter().toVector2f();
        Vector2f[] s1Axes = shape.getOutsideVectors();

        List<Vector2f> combined = new ArrayList<>(Arrays.stream(s1Axes).toList());
        // The perpendicular axis between the shape's center and the circle's center
        combined.add(shape.getTransform().getCenter().toVector2f().sub(circleCenterVector).normalize().perpendicular());

        Vector2f[] s1Vertices = ArrayUtils.toVector2fArray(shape.getExteriorVertices());

        float circleRadius = circle.getRadius() * circle.getTransform().getScaleX();

        for (Vector2f axis : combined) {
            axis.perpendicular();

            Range s1Range = projectShapeOntoAxis(s1Vertices, axis);
            float projectedCenter = circleCenterVector.dot(axis);
            Range circleRange = new Range(projectedCenter - circleRadius, projectedCenter + circleRadius);

            // If the ranges do not intersect, the shapes are not colliding
            if (!s1Range.intersects(circleRange)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks for collision between a shape and a circle. If the circle object is transformed into an ellipse, this
     * method will not work correctly, instead assuming that the radius of the circle is equal to the ellipsis's X
     * radius. If you want to check for collision between a shape and an ellipse, cast the ellipse to a shape and
     * use the Shape.collides(Shape, Shape) method.
     *
     * @param shape The shape object.
     * @param circle The circle object.
     * @return If they collide.
     */
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
     * Checks if collision between two circles. Note that if one or two of the two circles are transformed into ellipses,
     * this method will not work. Instead, cast the ellipses to shapes and use the Shape.collides(Shape, Shape) or
     * Shape.collides(Shape, Circle) methods.
     *
     * @param circle1 The first circle.
     * @param circle2 The second circle.
     * @return If they collide.
     */
    public static boolean collides(Circle circle1, Circle circle2) {
        return collides(
                circle1.getTransform().getCenter(), circle1.getRadius() * circle1.getTransform().getScaleX(),
                circle2.getTransform().getCenter(), circle2.getRadius() * circle2.getTransform().getScaleX()
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

    /**
     * Checks collision between a circle and a point. Note that if the circle is transformed into an ellipse, this
     * method will not work: it will assume the radius of the circle is the x-axis's radius.
     * Instead, cast the ellipse to a shape and use the Shape.collides(Shape, WorldCoords).
     *
     * @param circle The circle.
     * @param point The point to check collision with.
     * @return If they collide.
     */
    public static boolean collides(Circle circle, WorldCoords point) {
        WorldCoords circleCenter = circle.getTransform().getCenter();

        double radiusSquared = Math.pow(circle.getRadius() * circle.getTransform().getScaleX(), 2);
        double distSquared = Math.pow(circleCenter.x - point.x, 2) + Math.pow(circleCenter.y - point.y, 2);

        return distSquared <= radiusSquared;
    }

    /**
     * Checks collision between a circle and a point. Note that if the circle is transformed into an ellipse, this
     * method will not work. Instead, cast the ellipse to a shape and use the Shape.collides(Shape, WorldCoords).
     *
     * @param circle The circle.
     * @param point The point to check collision with.
     * @return If they collide.
     */
    public static boolean collides(WorldCoords point, Circle circle) {
        return collides(circle, point);
    }

    /**
     * Check if the shape is off-screen. If it is, then it's best not to waste a draw call on it
     *
     * @return If this object should be culled
     */
    protected boolean shouldDraw() {
        // Adjust for the edge case if the shape doesn't obey the camera
        WorldCoords center;

        boolean obeysCamera = ShaderProgram.getBoundProgram().getVertexShader().isObeyingCamera();
        if (obeysCamera) {
            center = Camera.getCenter();
        } else {
            center = WorldCoords.getMiddle();
        }

        // The farthest coordinate of the shape
        Vector2f farthestCoordinate = ArrayUtils.getFarthestPointFrom(
                ArrayUtils.toVector2fArray(this.getExteriorVertices()),
                this.getTransform().getCenter().toVector2f()
        );

        float radius = farthestCoordinate.distance(this.getTransform().getCenter().toVector2f());


        WorldCoords cameraTopRight = WorldCoords.getTopRight();
        WorldCoords cameraBottomLeft = new WorldCoords(0,0);

        if (obeysCamera) {
             cameraTopRight = Camera.adjustForCamera(cameraTopRight);
             cameraBottomLeft = Camera.adjustForCamera(cameraBottomLeft);
        }

        float windowRadius = cameraTopRight.toVector2f().distance(cameraBottomLeft.toVector2f());
        return collides(center, windowRadius, this.getTransform().getCenter(), radius);
    }

    /**
     * Binds a shader if none is already active.
     */
    protected void bindShader() {
        if (ShaderProgram.getBoundProgram() == null) {
            defaultShader.bind();
        }
    }

    public void draw() {
        ShaderProgram boundProgram = ShaderProgram.getBoundProgram();
        VertexShader vertexShader = boundProgram.getVertexShader();

        vertexShader.setMatrixUniforms(
                boundProgram.getProgramID(),
                this.transform.getMatrix()
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

    public abstract int[] getIndices();

    public float[] getTexCoords() {
        float[] vertices = this.calculateVertices();

        float xMin = ArrayUtils.getMin(ArrayUtils.getEvenIndices(vertices));
        float xMax = ArrayUtils.getMax(ArrayUtils.getEvenIndices(vertices));
        float yMin = ArrayUtils.getMin(ArrayUtils.getOddIndices(vertices));
        float yMax = ArrayUtils.getMax(ArrayUtils.getOddIndices(vertices));

        float[] texCoords = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            float min;
            float max;
            float texRepeat;

            // even = this is an x value, odd = this is a y value
            if (i % 2 == 0) {
                min = xMin;
                max = xMax;
                texRepeat = this.texRepeatX;

            } else {
                min = yMin;
                max = yMax;
                texRepeat = this.texRepeatY;
            }

            // uv = (value - min) / (max - min)
            texCoords[i] = (vertices[i] - min) / (max - min) * texRepeat;

            // Since the y-axis is flipped for some reason
            if (i % 2 != 0) {
                texCoords[i] = 1 - texCoords[i];
            }
        }

        return texCoords;
    }

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

    public static Vector2f[] getOutsideVectors(Vector2f[] exteriorVertices) {
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

    public Vector2f[] getOutsideVectors() {
        Vector2f[] exteriorVertices = ArrayUtils.toVector2fArray(this.getExteriorVertices());
        return getOutsideVectors(exteriorVertices);
    }

    /**
     * @param repeatTimes The amount of times for the texture to repeat over the y-axis.
     */
    public void setTexRepeatX(float repeatTimes) {
        this.texRepeatX = repeatTimes;

        // This case happens when the shape isn't fully initialized yet
        if (this.model == null) {
            return;
        }

        TexturedModel texturedModel = (TexturedModel) this.model;
        texturedModel.subTexCoords(this.getTexCoords(), 0);
    }

    /**
     * @param repeatTimes The amount of times for the texture to repeat over the x-axis.
     */
    public void setTexRepeatY(float repeatTimes) {
        this.texRepeatY = repeatTimes;

        // This case happens when the shape isn't fully initialized yet
        if (this.model == null) {
            return;
        }

        TexturedModel texturedModel = (TexturedModel) this.model;
        texturedModel.subTexCoords(this.getTexCoords(), 0);
    }

    public float getTexRepeatX() {
        return this.texRepeatX;
    }

    public float getTexRepeatY() {
        return this.texRepeatY;
    }
}
