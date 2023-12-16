package demos.rotationdemo;

import jangl.coords.WorldCoords;
import jangl.coords.PixelCoords;
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

    public void pointTo(WorldCoords coords) {
        PixelCoords pixelCoords = coords.toPixelCoords();
        PixelCoords center = this.shape.getTransform().getCenter().toPixelCoords();

        center.x -= pixelCoords.x;
        center.y -= pixelCoords.y;

        double angle = Math.atan2(center.y, center.x);
        this.shape.getTransform().setRotation((float) (angle + this.offset));
    }
}
