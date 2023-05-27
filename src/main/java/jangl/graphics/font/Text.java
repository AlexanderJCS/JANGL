package jangl.graphics.font;

import jangl.coords.NDCoords;
import jangl.coords.PixelCoords;
import jangl.graphics.Texture;
import jangl.graphics.font.parser.CharInfo;
import jangl.graphics.font.parser.Font;
import jangl.graphics.models.TexturedModel;

import java.util.Scanner;

public class Text implements AutoCloseable {
    private TexturedModel model;
    private String text;
    private NDCoords topLeft;
    private Font font;
    private float yHeight;

    /**
     * @param topLeft The top left coordinate of the text
     * @param font    The font to use
     * @param yHeight How high, in NDCoords, each letter should be
     * @param text    The text to display
     */
    public Text(NDCoords topLeft, Font font, float yHeight, String text) {
        this.topLeft = topLeft;
        this.yHeight = yHeight;
        this.font = font;
        this.text = this.pruneText(text);

        this.model = this.getModel();
    }

    public String getText() {
        return this.text;
    }

    /**
     * Removes any chars that cannot be displayed from the string.
     * @return The pruned text.
     */
    public String pruneText(String text) {
        StringBuilder builder = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (ch == '\t') {
                builder.append("    ");
            } else if (this.font.getInfo(ch) != null || ch == '\n') {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    public TexturedModel getModel() {
        // Use the capital letter A to find the scale
        int aHeightPixels = this.font.tallestLetter.height();
        float aHeightScreenCoords = PixelCoords.distYtoNDC(aHeightPixels);

        // desired height = current height * scale -> scale = desired height / current height
        float scaleFactor = this.yHeight / aHeightScreenCoords;

        // The cursor is where the next char should be drawn
        PixelCoords cursor = this.topLeft.toPixelCoords();

        float[] vertices = new float[this.text.length() * 8];
        float[] texCoords = new float[this.text.length() * 8];
        int[] indices = new int[this.text.length() * 6];

        for (int i = 0; i < this.text.length(); i++) {
            char ch = this.text.charAt(i);

            if (ch == '\n') {
                cursor.x = this.topLeft.toPixelCoords().x;
                cursor.y -= NDCoords.distYtoPixelCoords(this.yHeight) * 1.2;
                continue;
            }

            CharInfo info = this.font.getInfo(ch);

            if (info == null) {
                continue;
            }

            cursor.x += info.xOffset() * scaleFactor;
            cursor.y -= info.yOffset() * scaleFactor;

            NDCoords scCursor = cursor.toScreenCoords();
            float x1 = scCursor.x;
            float y1 = scCursor.y;
            float x2 = scCursor.x + PixelCoords.distXtoNDC(info.width()) * scaleFactor;
            float y2 = scCursor.y - PixelCoords.distYtoNDC(info.height()) * scaleFactor;

            float[] charVertices = new float[]{
                    x1, y1,
                    x2, y1,
                    x2, y2,
                    x1, y2
            };
            System.arraycopy(charVertices, 0, vertices, i * 8, charVertices.length);

            int[] charIndices = new int[]{
                    i * 4, i * 4 + 1, i * 4 + 2,
                    i * 4 + 2, i * 4 + 3, i * 4
            };
            System.arraycopy(charIndices, 0, indices, i * 6, charIndices.length);

            float[] charTexCoords = this.font.getTexCoords(ch);
            System.arraycopy(charTexCoords, 0, texCoords, i * 8, charTexCoords.length);

            cursor.x -= info.xOffset() * scaleFactor;
            cursor.y += info.yOffset() * scaleFactor;

            cursor.x += info.xAdvance() * scaleFactor;
        }

        return new TexturedModel(vertices, indices, texCoords);
    }

    /**
     * Regenerate the new model with any changes that may have been made since the last time it was generated
     */
    protected void regenerate() {
        this.close();  // close the old model before generating the new one
        this.model = this.getModel();
    }

    public void setText(String newText) {
        this.text = this.pruneText(newText);
        this.regenerate();
    }

    /**
     * WARNING: This method does not close the old font. It is recommended to run font.getFont().close() before calling
     * this method.
     *
     * @param font The new font.
     */
    public void setFont(Font font) {
        this.font = font;
        this.regenerate();
    }

    public void setYHeight(float newYHeight) {
        this.yHeight = newYHeight;
        this.regenerate();
    }

    public void setTopLeft(NDCoords newTopLeft) {
        this.topLeft = new NDCoords(newTopLeft.x, newTopLeft.y);
        this.regenerate();
    }

    /**
     * @return A copy of the top left coordinates
     */
    public NDCoords getTopLeft() {
        return new NDCoords(this.topLeft.x, this.topLeft.y);
    }

    /**
     * @return The font object being used.
     */
    public Font getFont() {
        return font;
    }

    /**
     * @return The y height, in normalized device coords, of the tallest letter in the font.
     *         Other letters are proportional.
     */
    public float getYHeight() {
        return yHeight;
    }

    public void draw() {
        this.font.fontTexture.bind();
        this.model.render();
        Texture.unbind();
    }

    @Override
    public void close() {
        this.model.close();
    }
}
