package jangl.graphics.font;

import io.github.pixee.security.BoundedLineReader;
import jangl.color.Color;
import jangl.color.ColorFactory;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.FontShader;
import jangl.graphics.shaders.premade.TextureShaderVert;
import jangl.graphics.textures.Texture;
import jangl.graphics.textures.TextureBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Font implements AutoCloseable {
    public final Texture fontTexture;
    public final CharInfo tallestLetter;
    private final Map<Integer, float[]> texCoordsMap;
    private final Map<Integer, CharInfo> infoMap;
    private final ShaderProgram shaderProgram;
    private final FontShader fontShader;

    /**
     * @param fontFile  The .fnt file of your font
     * @param fontImage The associated .png image associated with that .fnt file
     * @throws UncheckedIOException if the fontFile or fontImage is not found
     */
    public Font(String fontFile, String fontImage) throws UncheckedIOException {
        this.texCoordsMap = new HashMap<>();
        this.infoMap = new HashMap<>();
        this.fontShader = new FontShader(ColorFactory.fromNormalized(1, 1, 1, 1));
        this.shaderProgram = new ShaderProgram(new TextureShaderVert(), this.fontShader, TextureShaderVert.getAttribLocations());

        this.fontTexture = new Texture(new TextureBuilder().setImagePath(fontImage));
        this.fontTexture.useDefaultShader(false);

        BufferedImage glyphImage = readGlyphImage(fontImage);
        int glyphImageWidth = glyphImage.getWidth();
        int glyphImageHeight = glyphImage.getHeight();

        try (BufferedReader reader = new BufferedReader(new FileReader(fontFile))) {
            processFontFile(reader, glyphImageWidth, glyphImageHeight);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        this.tallestLetter = getTallestLetter();
    }

    private BufferedImage readGlyphImage(String fontImage) throws UncheckedIOException {
        try {
            return ImageIO.read(new File(fontImage));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void processFontFile(BufferedReader reader, int glyphImageWidth, int glyphImageHeight) throws IOException {
        for (String line = BoundedLineReader.readLine(reader, 1000000); line != null; line = BoundedLineReader.readLine(reader, 1000000)) {
            if (line.startsWith("char") && !line.contains("count")) {
                CharInfo info = parseLine(line);
                float uvTopLeftX = (float) info.x() / glyphImageWidth;
                float uvTopLeftY = (float) info.y() / glyphImageHeight;
                float uvWidth = (float) info.width() / glyphImageWidth;
                float uvHeight = (float) info.height() / glyphImageHeight;

                float[] texCoords = new float[]{
                        uvTopLeftX, uvTopLeftY, // top left
                        uvTopLeftX + uvWidth, uvTopLeftY, // top right
                        uvTopLeftX + uvWidth, uvTopLeftY + uvHeight, // bottom right
                        uvTopLeftX, uvTopLeftY + uvHeight // bottom left
                };

                this.texCoordsMap.put(info.charID(), texCoords);
                this.infoMap.put(info.charID(), info);
            }
        }
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

    ShaderProgram getShaderProgram() {
        return this.shaderProgram;
    }

    public void setFontColor(Color color) {
        this.fontShader.setColor(color);
    }

    public Color getFontColor() {
        return this.fontShader.getColor();
    }

    public void setObeyCamera(boolean obeyCamera) {
        this.shaderProgram.getVertexShader().setObeyCamera(obeyCamera);
    }

    public boolean isObeyingCamera() {
        return this.shaderProgram.getVertexShader().isObeyingCamera();
    }

    /**
     * Set if the default colors of the font should be kept, or if it should be replaced with the current font color.
     * This is useful if the font has an outline or some other special color information that you want to display.
     *
     * @param keepDefaultColors True to keep the default colors of the font, false to use the current font color.
     */
    public void setKeepDefaultColors(boolean keepDefaultColors) {
        this.fontShader.setKeepDefaultColors(keepDefaultColors);
    }

    /**
     * @return True if the font's default color is being displayed. False if a custom color is being displayed.
     */
    public boolean isKeepingDefaultColors() {
        return this.fontShader.isKeepingDefaultColors();
    }

    @Override
    public void close() {
        this.fontTexture.close();
        this.shaderProgram.close();
    }
}
