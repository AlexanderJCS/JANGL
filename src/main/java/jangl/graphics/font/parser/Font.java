package jangl.graphics.font.parser;

import jangl.graphics.Texture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class Font implements AutoCloseable {
    private final Map<Integer, Texture> textureMap;
    private final Map<Integer, CharInfo> infoMap;

    /**
     * @param fontFile  The .fnt file of your font
     * @param fontImage The associated .png image associated with that .fnt file
     */
    public Font(String fontFile, String fontImage) {
        this.textureMap = new HashMap<>();
        this.infoMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fontFile))) {

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                // The line must start with "char" and must not contain "count"
                if (!line.startsWith("char") || line.contains("count")) {
                    continue;
                }

                CharInfo info = this.parseLine(line);

                this.textureMap.put(
                        info.charID(),
                        // The GL_LINEAR filter is important in order to make the text look "smoother"
                        new Texture(fontImage, info.x(), info.y(), info.width(), info.height())
                );

                this.infoMap.put(
                        info.charID(), info
                );
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
     * @return The texture of the character
     */
    public Texture getTexture(int id) {
        return this.textureMap.get(id);
    }

    /**
     * @param ch The character to get the texture of
     * @return The texture of the character
     */
    public Texture getTexture(char ch) {
        return this.getTexture((int) ch);
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
        for (Texture texture : textureMap.values()) {
            texture.close();
        }

        this.textureMap.clear();
    }
}
