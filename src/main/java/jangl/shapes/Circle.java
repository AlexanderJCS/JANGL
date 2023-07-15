package jangl.shapes;

import jangl.coords.NDCoords;
import jangl.coords.PixelCoords;
import jangl.graphics.models.Model;
import jangl.graphics.models.TriangleFanModel;

import java.util.Arrays;

public class Circle extends Shape {
    private final int sides;
    private NDCoords center;
    // X and Y radius need to be different since the screen may not be square
    private float radiusX;
    private float radiusY;

    /**
     * Since one ScreenCoord on the x-axis is not the same as one ScreenCoord on the y-axis if the aspect ratio
     * is not 1:1, the radius will be in the units of X-axis NDCoords.
     *
     * @param center The center of the circle.
     * @param radius The X-radius of the circle (see the above note)
     * @param sides  The number of sides of the shape.
     *
     * @throws IllegalArgumentException Throws if the number of sides on the circle is less than 3.
     */
    public Circle(NDCoords center, float radius, int sides) throws IllegalArgumentException {
        if (sides <= 2) {
            throw new IllegalArgumentException("A circle must have 3 or more sides, not " + sides);
        }

        this.center = center;

        this.sides = sides;
        this.radiusX = radius;
        this.radiusY = PixelCoords.distYtoNDC(NDCoords.distXtoPixelCoords(radius));
        this.model = this.toModel();
    }

    /**
     * @param newRadius The new radius
     */
    public void setRadius(float newRadius) {
        this.radiusX = newRadius;
        this.radiusY = PixelCoords.distYtoNDC(NDCoords.distXtoPixelCoords(newRadius));
        this.model.subVertices(this.calculateVertices(), 0);
    }

    @Override
    public void shift(float x, float y) {
        this.center.x += x;
        this.center.y += y;

        this.model.subVertices(this.calculateVertices(), 0);
    }

    @Override
    public NDCoords getCenter() {
        return new NDCoords(
                Shape.rotateAxis(new float[]{this.center.x, this.center.y}, this.axisAngle)
        );
    }

    public void setCenter(NDCoords newCenter) {
        this.center = newCenter;
        this.model.subVertices(this.calculateVertices(), 0);
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
        return new TriangleFanModel(this.calculateVertices());
    }

    @Override
    public void draw() {
        super.draw();
        this.model.render();
    }

    @Override
    public float[] calculateVertices() {
        float[] vertices = new float[2 * (this.sides + 2)];

        vertices[0] = this.center.x;
        vertices[1] = this.center.y;

        for (int i = 1; i < vertices.length / 2; i++) {
            vertices[i * 2] = (float) (this.center.x + (this.radiusX * Math.cos(i * 2 * Math.PI / this.sides)));
            vertices[i * 2 + 1] = (float) (this.center.y + (this.radiusY * Math.sin(i * 2 * Math.PI / this.sides)));
        }

        return Shape.rotateAxis(Shape.rotateLocal(vertices, this.getCenter(), this.localAngle), this.axisAngle);
    }

    @Override
    public float[] getExteriorVertices() {
        float[] vertices = this.calculateVertices();
        return Arrays.copyOfRange(vertices, 2, vertices.length - 2);
    }

    @Override
    public void close() {
        this.model.close();
    }
}
