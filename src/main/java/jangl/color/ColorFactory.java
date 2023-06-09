package jangl.color;

public class ColorFactory {
    private ColorFactory() {

    }

    public static Color from255(int red, int green, int blue, int alpha) throws IllegalArgumentException {
        if (red > 255 || red < 0) {
            throw new IllegalArgumentException("Red color should be between [0, 255].");
        } else if (green > 255 || green < 0) {
            throw new IllegalArgumentException("Green color should be between [0, 255].");
        } else if (blue > 255 || blue < 0) {
            throw new IllegalArgumentException("Blue color should be between [0, 255].");
        } else if (alpha > 255 || alpha < 0) {
            throw new IllegalArgumentException("Alpha color should be between [0, 255].");
        }

        float redNormalized = red / 255f;
        float greenNormalized = green / 255f;
        float blueNormalized = blue / 255f;
        float alphaNormalized = alpha / 255f;

        return new Color(redNormalized, greenNormalized, blueNormalized, alphaNormalized);
    }

    public static Color fromNormalized(float red, float green, float blue, float alpha) throws IllegalArgumentException {
        if (red > 1 || red < 0) {
            throw new IllegalArgumentException("Normalized red colors should be between 0 and 1");
        } else if (green > 1 || green < 0) {
            throw new IllegalArgumentException("Normalized green colors should be between 0 and 1");
        } else if (blue > 1 || blue < 0) {
            throw new IllegalArgumentException("Normalized blue colors should be between 0 and 1");
        } else if (alpha > 1 || alpha < 0) {
            throw new IllegalArgumentException("Normalized alpha colors should be between 0 and 1");
        }

        return new Color(red, green, blue, alpha);
    }
}
