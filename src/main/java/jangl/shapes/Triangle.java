package jangl.shapes;

import jangl.coords.ScreenCoords;
import jangl.graphics.models.Model;

public class Triangle extends Shape {
    private final ScreenCoords point1;
    private final ScreenCoords point2;
    private final ScreenCoords point3;

    public Triangle(ScreenCoords point1, ScreenCoords point2, ScreenCoords point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;

        this.model = this.toModel();
    }

    @Override
    public ScreenCoords getCenter() {
        float xSum = this.point1.x + this.point2.x + this.point3.x;
        float ySum = this.point1.y + this.point2.y + this.point3.y;

        return new ScreenCoords(xSum / 3, ySum / 3);
    }

    @Override
    public void shift(float x, float y) {
        this.point1.x += x;
        this.point2.x += x;
        this.point3.x += x;

        this.point1.y += y;
        this.point2.y += y;
        this.point3.y += y;
    }

    @Override
    public float[] calculateVertices() {
        return Shape.rotateAxis(
                Shape.rotateLocal(
                        new float[] {
                                this.point1.x, this.point1.y,
                                this.point2.x, this.point2.y,
                                this.point3.x, this.point3.y
                        },
                        this.getCenter(),
                        this.localAngle
                ),
                this.axisAngle
        );
    }

    @Override
    public void draw() {
        this.model.render();
    }

    @Override
    public float[] getExteriorVertices() {
        return this.calculateVertices();
    }

    @Override
    public void close() {
        this.model.close();
    }

    private Model toModel() {
        return new Model(this.calculateVertices());
    }
}
