package jangl.graphics.font;

/**
 * @param charID   The ASCII ID of the character
 * @param xOffset  The x offset, in pixels, for the character to display correctly.
 * @param yOffset  The y offset, in pixels, for the character to display correctly.
 * @param xAdvance How much to move the cursor, in pixels, for the next character to display properly.
 * @param x        The x coordinate, in pixels, of the character in the ASCII image
 * @param y        The y coordinate, in pixels, of the character in the ASCII image
 * @param width    The width of the character, in pixels, in the ASCII image
 * @param height   The height of the character, in pixels, in the ASCII image
 */
public record CharInfo(int charID, int xOffset, int yOffset, int xAdvance, int x, int y, int width, int height) {
}
