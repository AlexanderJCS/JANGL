package jglt.coords;

public class Range {
    public final float min;
    public final float max;

    public Range(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public boolean otherInside(Range other) {
        return other.max < this.max && other.max > this.min ||
                other.min < this.max && other.min > this.min;
    }

    public boolean collidesWith(Range other) {
        return this.otherInside(other) || other.otherInside(this);
    }

    @Override
    public String toString() {
        return "Range{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
