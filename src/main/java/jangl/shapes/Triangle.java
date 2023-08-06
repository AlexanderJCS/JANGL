package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.models.Model;
import jangl.graphics.models.TexturedModel;

public class Triangle extends Shape {
    private final WorldCoords point1;
    private final WorldCoords point2;
    private final WorldCoords point3;

    public Triangle(WorldCoords point1, WorldCoords point2, WorldCoords point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;

        WorldCoords center = this.getCenter();
        this.point1.x -= center.x;
        this.point1.y -= center.y;
        this.point2.x -= center.x;
        this.point2.y -= center.y;
        this.point3.x -= center.x;
        this.point3.y -= center.y;
        this.transform.shift(center);

        this.model = this.toTexturedModel();
    }

    private WorldCoords getCenter() {
        float xSum = this.point1.x + this.point2.x + this.point3.x;
        float ySum = this.point1.y + this.point2.y + this.point3.y;

        return new WorldCoords(xSum / 3, ySum / 3);
    }

    @Override
    public float[] calculateVertices() {
        return new float[]{
                this.point1.x, this.point1.y,
                this.point2.x, this.point2.y,
                this.point3.x, this.point3.y
        };
    }

    protected int[] getIndices() {
        return new int[]{0, 1, 2};
    }

    protected float[] getTexCoords() {
        float xMin = Math.min(this.point1.x, Math.min(this.point2.x, this.point3.x));
        float xMax = Math.max(this.point1.x, Math.max(this.point2.x, this.point3.x));
        float yMin = Math.min(this.point1.y, Math.min(this.point2.y, this.point3.y));
        float yMax = Math.max(this.point1.y, Math.max(this.point2.y, this.point3.y));

        float[] vertices = this.calculateVertices();
        float[] texCoords = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            float x = vertices[i];

            float min;
            float max;
            // even = this is an x value, odd = this is a y value
            if (i % 2 == 0) {
                min = xMin;
                max = xMax;
            } else {
                min = yMin;
                max = yMax;
            }

            // uv = (value - min) / (max - min)
            texCoords[i] = (vertices[i] - min) / (max - min);
        }

        return texCoords;
    }

    @Override
    public void draw() {
        super.bindShader();
        if (super.shouldDraw()) {
            super.draw();
            this.model.render();
        }
    }

    @Override
    public float[] getExteriorVertices() {
        return this.calculateVerticesMatrix();
    }

    @Override
    public void close() {
        this.model.close();
    }

    private Model toTexturedModel() {
        return new TexturedModel(this.calculateVertices(), this.getIndices(), this.getTexCoords());
    }
}
