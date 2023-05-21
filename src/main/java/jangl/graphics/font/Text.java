package jangl.graphics.font;

import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.graphics.Image;
import jangl.graphics.font.parser.CharInfo;
import jangl.graphics.font.parser.Font;
import jangl.shapes.Rect;

import java.util.ArrayList;
import java.util.List;

public class Text implements AutoCloseable {
    private final List<Image> characters;
    private String text;
    private ScreenCoords topLeft;
    private Font font;
    private float yHeight;

    /**
     * @param font    The font to use
     * @param text    The text to display
     * @param topLeft The top left coordinate of the text
     * @param yHeight How high, in screen coords, each letter should be
     */
    public Text(Font font, String text, ScreenCoords topLeft, float yHeight) {
        this.characters = new ArrayList<>();
        this.topLeft = topLeft;
        this.yHeight = yHeight;
        this.font = font;

        this.setText(text);
    }

    public String getText() {
        return this.text;
    }

    /**
     * Set the text to be displayed to the screen.
     *
     * @param text The text string to be displayed to the screen.
     */
    public void setText(String text) {
        this.close();  // clear the characters list and close all images
        this.text = text;

        // Use the capital letter A to find the scale
        int aHeightPixels = this.font.getInfo('A').height();
        float aHeightScreenCoords = PixelCoords.distYtoScreenDist(aHeightPixels);

        // desired height = current height * scale -> scale = desired height / current height
        float scaleFactor = this.yHeight / aHeightScreenCoords;

        // The cursor is where the next char should be drawn
        PixelCoords cursor = this.topLeft.toPixelCoords();

        for (char ch : text.toCharArray()) {
            CharInfo info = this.font.getInfo(ch);

            if (info == null) {
                continue;
            }

            cursor.x += info.xOffset() * scaleFactor;
            cursor.y -= info.yOffset() * scaleFactor;

            this.characters.add(
                    new Image(
                            new Rect(
                                    cursor.toScreenCoords(),
                                    PixelCoords.distXtoScreenDist(info.width()) * scaleFactor,
                                    PixelCoords.distYtoScreenDist(info.height()) * scaleFactor
                            ),
                            font.getTexture(ch)
                    )
            );

            cursor.x -= info.xOffset() * scaleFactor;
            cursor.y += info.yOffset() * scaleFactor;

            cursor.x += info.xAdvance() * scaleFactor;
        }
    }

    /**
     * @return A copy of the top left screen coordinates
     */
    public ScreenCoords getTopLeft() {
        return new ScreenCoords(this.topLeft.x, this.topLeft.y);
    }

    /**
     * Set the top left coordinate of the text.
     *
     * @param topLeft The top left coordinate of the text.
     */
    public void setTopLeft(ScreenCoords topLeft) {
        // Shift the rects by the delta instead of recalculating the text
        // This will save processing time
        float deltaX = topLeft.x - this.topLeft.y;
        float deltaY = topLeft.y - this.topLeft.y;

        for (Image character : this.characters) {
            character.rect().shift(deltaX, deltaY);
        }

        this.topLeft = topLeft;
    }

    /**
     * @return The font object being used.
     */
    public Font getFont() {
        return font;
    }

    /**
     * @param font The new font to set the text to.
     */
    public void setFont(Font font) {
        this.font = font;
        this.setText(this.getText());  // recalculate text with the new font
    }

    /**
     * @return The y height, in screen coordinates, of the capital A letter. Other letters are proportional.
     */
    public float getYHeight() {
        return yHeight;
    }

    /**
     * Set the y height, in y screen coordinates, of the text. The y height is measured for the letter capital A.
     *
     * @param yHeight The y height of the text.
     */
    public void setYHeight(float yHeight) {
        this.yHeight = yHeight;
        this.setText(this.getText());  // recalculate text with the new y height
    }

    public void draw() {
        for (Image charImage : this.characters) {
            charImage.draw();
        }
    }

    @Override
    public void close() {
        for (Image character : this.characters) {
            character.rect().close();
        }

        this.characters.clear();
    }
}
