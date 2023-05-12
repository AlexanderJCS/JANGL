package jangl.util;

public record Range(float min, float max) {

    /**
     * @param other The other range to check
     * @return Whether the other range is partially or fully inside this range, exclusive of the edges. Does not
     *         return true if this is fully inside the other range. For that, use the Range.intersects() method.
     */
    public boolean contains(Range other) {
        return other.max < this.max && other.max > this.min ||
                other.min < this.max && other.min > this.min;
    }

    /**
     * @param other The other range to check
     * @return Whether this and other intersect
     */
    public boolean intersects(Range other) {
        return this.contains(other) || other.contains(this);
    }
}
