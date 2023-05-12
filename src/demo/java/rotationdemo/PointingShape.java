package rotationdemo;

import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.shapes.Shape;

public class PointingShape {
    private final Shape shape;
    private final double offset;

    public PointingShape(Shape shape, double offset) {
        this.shape = shape;
        this.offset = offset;
    }

    public Shape getShape() {
        return this.shape;
    }

    public void pointTo(ScreenCoords coords) {
        PixelCoords pixelCoords = coords.toPixelCoords();
        PixelCoords center = this.shape.getCenter().toPixelCoords();

        center.x -= pixelCoords.x;
        center.y -= pixelCoords.y;

        double angle = Math.atan2(center.y, center.x);
        this.shape.setLocalAngle(-angle + this.offset);
    }
}
