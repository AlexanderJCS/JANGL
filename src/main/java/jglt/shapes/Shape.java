package jglt.shapes;

import jglt.coords.ScreenCoords;

public interface Shape {
    void draw();
    void shift(float x, float y);
    float[] getVertices();
    ScreenCoords getCenter();

    boolean collidesWith(Rect rect);
    boolean collidesWith(Circle circle);
}
