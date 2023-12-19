package jangl.graphics.textures;

import jangl.graphics.Bindable;
import jangl.shapes.Shape;

/**
 * A convenient wrapper to allow for multiple bindables on a single object. Bindables can include textures, shaders, etc
 */
public class Image {
    private final Shape shape;
    private final Bindable bindable1;
    private final Bindable bindable2;

    public Image(Shape shape, Bindable bindable1) {
        this(shape, bindable1, null);
    }

    public Image(Shape shape, Bindable bindable1, Bindable bindable2) {
        this.shape = shape;
        this.bindable1 = bindable1;
        this.bindable2 = bindable2;
    }

    public void draw() {
        if (this.bindable1 != null) {
            this.bindable1.bind();
        } if (this.bindable2 != null) {
            this.bindable2.bind();
        }

        this.shape.draw();

        if (this.bindable1 != null) {
            this.bindable1.unbind();
        } if (this.bindable2 != null) {
            this.bindable2.unbind();
        }
    }

    public Shape shape() {
        return this.shape;
    }

    public Bindable bindable1() {
        return this.bindable1;
    }

    public Bindable bindable2() {
        return this.bindable2;
    }
}
