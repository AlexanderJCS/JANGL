package demos.rotationdemo;

import jangl.JANGL;
import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import jangl.shapes.Circle;

import java.util.ArrayList;
import java.util.List;

public class RotationDemo implements AutoCloseable {
    private final List<PointingShape> pointingShapes;
    private final ShaderProgram colorShader;

    public RotationDemo() {
        JANGL.init(1600, 900);
        Window.setVsync(true);

        this.colorShader = new ShaderProgram(new ColorShader(ColorFactory.fromNorm(0.9f, 0.5f, 0.2f, 1)));

        this.pointingShapes = new ArrayList<>();
        this.pointingShapes.add(new PointingShape(new Circle(new WorldCoords(WorldCoords.getMiddle().x, 0.2f), 0.1f, 3), 1.0472));
        this.pointingShapes.add(new PointingShape(new Circle(new WorldCoords(WorldCoords.getMiddle().x, 0.5f), 0.1f, 3), 1.0472));
        this.pointingShapes.add(new PointingShape(new Circle(new WorldCoords(WorldCoords.getMiddle().x, 0.8f), 0.1f, 3), 1.0472));
    }

    public void update() {
        for (PointingShape pointingShape : pointingShapes) {
            pointingShape.pointTo(Mouse.getMousePos());
        }

        JANGL.update();
    }

    public void draw() {
        Window.clear();

        for (PointingShape pointingShape : pointingShapes) {
            pointingShape.getShape().draw(this.colorShader);
        }
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();
            this.update();
        }

        Window.close();
    }

    @Override
    public void close() {
        this.colorShader.close();

        for (PointingShape pointingShape : pointingShapes) {
            try {
                pointingShape.getShape().close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        try (RotationDemo rd = new RotationDemo()) {
            rd.run();
        }
    }
}
