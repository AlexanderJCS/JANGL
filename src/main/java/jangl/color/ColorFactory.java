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

    public static Color from255(int[] rgba) {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA array must be of length 4");
        }

        return from255(rgba[0], rgba[1], rgba[2], rgba[3]);
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

    public static Color fromNormalized(float[] rgba) {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA array must be of length 4");
        }

        return fromNormalized(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    /**
     * Converts HSVA (hue, saturation, value, alpha) to a Color object.
     *
     * @param hue        The hue value. Can be any floating-point value, but 0 = min and 1 = max.
     * @param saturation The normalized saturation of the color
     * @param value      The normalized value (brightness) of the color
     * @param alpha      The normalized alpha of the color
     * @return A new JANGL color object.
     * @throws IllegalArgumentException If any of the four arguments are not between 0 and 1
     */
    public static Color fromNormalizedHSVA(float hue, float saturation, float value, float alpha) throws IllegalArgumentException {
        if (saturation > 1 || saturation < 0) {
            throw new IllegalArgumentException("Normalized saturation colors should be between 0 and 1");
        } else if (value > 1 || value < 0) {
            throw new IllegalArgumentException("Normalized value colors should be between 0 and 1");
        } else if (alpha > 1 || alpha < 0) {
            throw new IllegalArgumentException("Normalized alpha colors should be between 0 and 1");
        }

        java.awt.Color rgb = new java.awt.Color(java.awt.Color.HSBtoRGB(hue, saturation, value));

        // return a JANGL color instead of java.awt.Color color
        return new Color(rgb.getRed() / 255f, rgb.getGreen() / 255f, rgb.getBlue() / 255f, alpha);
    }
}
