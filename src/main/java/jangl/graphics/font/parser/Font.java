package jangl.graphics.font.parser;

import jangl.graphics.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Font implements AutoCloseable {
    public final Texture fontTexture;
    private final Map<Integer, float[]> texCoordsMap;
    private final Map<Integer, CharInfo> infoMap;
    public final CharInfo tallestLetter;

    /**
     * @param fontFile  The .fnt file of your font
     * @param fontImage The associated .png image associated with that .fnt file
     *
     * @throws UncheckedIOException if the fontFile or fontImage is not found
     */
    public Font(String fontFile, String fontImage) throws UncheckedIOException {
        this.texCoordsMap = new HashMap<>();
        this.infoMap = new HashMap<>();

        this.fontTexture = new Texture(fontImage);

        int glyphImageWidth;
        int glyphImageHeight;

        try {
             BufferedImage glyphImage = ImageIO.read(new File(fontImage));

            glyphImageWidth = glyphImage.getWidth();
            glyphImageHeight = glyphImage.getHeight();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fontFile))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                // The line must start with "char" and must not contain "count"
                if (!line.startsWith("char") || line.contains("count")) {
                    continue;
                }

                CharInfo info = this.parseLine(line);

                // Calculate the texture coordinates
                float uvTopLeftX = (float) info.x() / glyphImageWidth;
                float uvTopLeftY = ((float) info.y() / glyphImageHeight);

                float uvWidth = (float) info.width() / glyphImageWidth;
                float uvHeight = (float) info.height() / glyphImageHeight;

                /*
                 * Gives texture coordinates in this order: top left, top right, bottom right, bottom left
                 * AKA clockwise order starting from the bottom left
                 */
                this.texCoordsMap.put(
                        info.charID(),
                        new float[]{
                                uvTopLeftX, uvTopLeftY,  // top left
                                uvTopLeftX + uvWidth, uvTopLeftY,  // top right
                                uvTopLeftX + uvWidth, uvTopLeftY + uvHeight,  // bottom right
                                uvTopLeftX, uvTopLeftY + uvHeight,  // bottom left
                        }
                );

                // Put metadata in the infoMap
                this.infoMap.put(
                        info.charID(), info
                );
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        this.tallestLetter = this.getTallestLetter();
    }

    /**
     * @return The CharInfo of the tallest letter in pixels.
     */
    private CharInfo getTallestLetter() {
        CharInfo max = null;

        for (CharInfo info : this.infoMap.values()) {
            if (max == null || info.height() > max.height()) {
                max = info;
            }
        }

        return max;
    }

    /**
     * @param line One line of the .fnt file to parse
     * @return The information from that line.
     */
    private CharInfo parseLine(String line) {
        // Replace all repeated spaces with a single space, then split by a single space
        String[] splitted = line.replaceAll("\\s+", " ").split(" ");

        Map<String, Integer> charInfo = new HashMap<>();

        // i = 1 to avoid the "char" substring of the line
        for (int i = 1; i < splitted.length; i++) {
            // index = 0: the var name | index = 1: the value
            String[] subSplitted = splitted[i].split("=");

            charInfo.put(subSplitted[0], Integer.parseInt(subSplitted[1]));
        }

        return new CharInfo(
                charInfo.get("id"),
                charInfo.get("xoffset"),
                charInfo.get("yoffset"),
                charInfo.get("xadvance"),
                charInfo.get("x"),
                charInfo.get("y"),
                charInfo.get("width"),
                charInfo.get("height")
        );
    }

    /**
     * @param id The ASCII ID of the character to get the texture of
     * @return The texture of the character in clockwise order, starting from the top left
     */
    public float[] getTexCoords(int id) {
        return this.texCoordsMap.get(id);
    }

    /**
     * @param ch The character to get the texture of
     * @return The texture coords of the character in clockwise order, starting from the top left.
     */
    public float[] getTexCoords(char ch) {
        return this.getTexCoords((int) ch);
    }

    /**
     * @param id The ASCII ID of the character to get information of
     * @return The CharInfo for that character
     */
    public CharInfo getInfo(int id) {
        return this.infoMap.get(id);
    }

    /**
     * @param ch The character to get information of
     * @return The CharInfo for that character
     */
    public CharInfo getInfo(char ch) {
        return this.getInfo((int) ch);
    }


    @Override
    public void close() {
        this.fontTexture.close();
    }
}
