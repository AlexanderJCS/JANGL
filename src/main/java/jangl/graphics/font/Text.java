package jangl.graphics.font;

import jangl.coords.PixelCoords;
import jangl.coords.ScreenCoords;
import jangl.graphics.Image;
import jangl.graphics.font.parser.CharInfo;
import jangl.graphics.font.parser.Font;
import jangl.shapes.Rect;

import java.util.ArrayList;
import java.util.List;

public class Text {
    private final List<Image> characters;

    public Text(Font font, String text, ScreenCoords topLeft) {
        this.characters = new ArrayList<>();

        // The cursor is where the next char should be drawn
        PixelCoords cursor = topLeft.toPixelCoords();

        for (char ch : text.toCharArray()) {
            CharInfo info = font.getInfo(ch);

            cursor.x += info.xOffset();
            cursor.y -= info.yOffset();

            characters.add(
                    new Image(
                            new Rect(
                                    cursor.toScreenCoords(),
                                    PixelCoords.distXtoScreenDist(info.width()),
                                    PixelCoords.distYtoScreenDist(info.height())
                            ),
                            font.getTexture(ch)
                    )
            );

            cursor.x -= info.xOffset();
            cursor.y += info.yOffset();

            cursor.x += info.xAdvance();
        }
    }

    public void draw() {
        for (Image charImage : characters) {
            charImage.draw();
        }
    }
}
