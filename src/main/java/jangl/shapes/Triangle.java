package jangl.shapes;

import jangl.coords.NDCoords;
import jangl.graphics.models.Model;

public class Triangle extends Shape {
    private final NDCoords point1;
    private final NDCoords point2;
    private final NDCoords point3;

    public Triangle(NDCoords point1, NDCoords point2, NDCoords point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;

        NDCoords center = this.getCenter();
        this.point1.x -= center.x;
        this.point1.y -= center.y;
        this.point2.x -= center.x;
        this.point2.y -= center.y;
        this.point3.x -= center.x;
        this.point3.y -= center.y;
        this.transform.shift(center.x, center.y);

        this.model = this.toModel();
    }

    @Override
    public NDCoords getCenter() {
        float xSum = this.point1.x + this.point2.x + this.point3.x;
        float ySum = this.point1.y + this.point2.y + this.point3.y;

        return new NDCoords(xSum / 3, ySum / 3);
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
                        new float[]{
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
        super.draw();
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
