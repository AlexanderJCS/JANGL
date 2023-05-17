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

    /**
     * @param font The font to use
     * @param text The text to display
     * @param topLeft The top left coordinate of the text
     * @param yHeight How high, in screen coords, each letter should be
     */
    public Text(Font font, String text, ScreenCoords topLeft, float yHeight) {
        this.characters = new ArrayList<>();

        // Use the capital letter A to find the scale
        int aHeightPixels = font.getInfo('A').height();
        float aHeightScreenCoords = PixelCoords.distYtoScreenDist(aHeightPixels);

        // desired height = current height * scale -> scale = desired height / current height
        float scaleFactor = yHeight / aHeightScreenCoords;

        // The cursor is where the next char should be drawn
        PixelCoords cursor = topLeft.toPixelCoords();

        for (char ch : text.toCharArray()) {
            CharInfo info = font.getInfo(ch);

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

    public void draw() {
        for (Image charImage : this.characters) {
            charImage.draw();
        }
    }

    @Override
    public void close() {
        for (Image character : this.characters) {
            character.getRect().close();
        }

        this.characters.clear();
    }
}
