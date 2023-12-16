package jangl.color;

public class ColorFactory {
    private ColorFactory() {

    }

    /**
     * Takes in a set of RGBA values between 0 and 255, inclusive and creates a JANGL color.
     *
     * @param red The red value.
     * @param green The green value.
     * @param blue The blue value.
     * @param alpha The alpha value.
     * @return A new JANGL color object.
     * @throws IllegalArgumentException If any of the four arguments are not between 0 and 255, inclusive
     */
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

    /**
     * @param rgba An array of RGBA values ranging between 0-255, inclusive.
     * @return The JANGL Color object.
     */
    public static Color from255(int[] rgba) {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA array must be of length 4");
        }

        return from255(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    /**
     * Takes in a set of RGBA values between 0 and 1 and creates a JANGL color.
     *
     * @param red The normalized red value.
     * @param green The normalized green value.
     * @param blue The normalized blue value.
     * @param alpha The normalized alpha value.
     * @return A new JANGL color object.
     * @throws IllegalArgumentException If any of the four arguments are not between 0 and 1, inclusive
     */
    public static Color fromNorm(float red, float green, float blue, float alpha) throws IllegalArgumentException {
        if (red > 1 || red < 0) {
            throw new IllegalArgumentException("Normalized red value should be between 0 and 1, inclusive");
        } else if (green > 1 || green < 0) {
            throw new IllegalArgumentException("Normalized green value should be between 0 and 1, inclusive");
        } else if (blue > 1 || blue < 0) {
            throw new IllegalArgumentException("Normalized blue value should be between 0 and 1, inclusive");
        } else if (alpha > 1 || alpha < 0) {
            throw new IllegalArgumentException("Normalized alpha value should be between 0 and 1, inclusive");
        }

        return new Color(red, green, blue, alpha);
    }

    public static Color fromNorm(float[] rgba) {
        if (rgba.length != 4) {
            throw new IllegalArgumentException("RGBA array must be of length 4");
        }

        return fromNorm(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    /**
     * Converts normalized HSVA (hue, saturation, value, alpha) values to a Color object. The range of inputs is [0, 1],
     * inclusive.
     *
     * @param hue        The normalized hue value.
     * @param saturation The normalized saturation of the color
     * @param value      The normalized value (brightness) of the color
     * @param alpha      The normalized alpha of the color
     * @return A new JANGL color object.
     * @throws IllegalArgumentException If any of the four arguments are not between 0 and 1, inclusive
     */
    public static Color fromNormHSVA(float hue, float saturation, float value, float alpha) throws IllegalArgumentException {
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

    /**
     * Converts 256 HSVA (hue, saturation, value, alpha) values to a Color object. The range of inputs is [0, 255],
     * inclusive.
     *
     * @param hue        The normalized hue value.
     * @param saturation The normalized saturation of the color
     * @param value      The normalized value (brightness) of the color
     * @param alpha      The normalized alpha of the color
     * @return A new JANGL color object.
     * @throws IllegalArgumentException If any of the four arguments are not between 0 and 255
     */
    public static Color from255HSVA(int hue, int saturation, int value, int alpha) {
        if (saturation > 1 || saturation < 0) {
            throw new IllegalArgumentException("Saturation should be between 0 and 255, inclusive");
        } else if (value > 1 || value < 0) {
            throw new IllegalArgumentException("Value should be between 0 and 255, inclusive");
        } else if (alpha > 1 || alpha < 0) {
            throw new IllegalArgumentException("Alpha should be between 0 and 255, inclusive");
        }

        return fromNormHSVA(hue / 255f, saturation / 255f, value / 255f, alpha / 255f);
    }
}
