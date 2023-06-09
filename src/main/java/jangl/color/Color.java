package jangl.color;

public class Color {
    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;

    Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    private int normTo255(float norm) {
        return (int) (norm * 255);
    }

    public float getNormRed() {
        return this.red;
    }

    public int get255Red() {
        return this.normTo255(this.red);
    }

    public float getNormGreen() {
        return this.green;
    }

    public int get255Green() {
        return this.normTo255(this.green);
    }

    public float getNormBlue() {
        return this.blue;
    }

    public int get255Blue() {
        return this.normTo255(this.blue);
    }

    public float getNormAlpha() {
        return this.alpha;
    }

    public int get255Alpha() {
        return this.normTo255(this.alpha);
    }

    public float[] getNormRGBA() {
        return new float[]{ this.getNormRed(), this.getNormGreen(), this.getNormBlue(), this.getNormAlpha() };
    }

    public int[] get255RGBA() {
        return new int[]{ this.get255Red(), this.get255Green(), this.get255Blue(), this.get255Alpha() };
    }
}
