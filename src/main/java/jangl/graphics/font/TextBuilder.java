package jangl.graphics.font;

import jangl.coords.WorldCoords;

public class TextBuilder {
    private String text;
    private WorldCoords coords;
    private Font font;
    private float height;
    private Justify justification;
    private float wrapWidth;
    private float yCutoff;

    public TextBuilder(Font font, String text) {
        this(font, text, WorldCoords.getMiddle());
    }

    public TextBuilder(Font font, String text, WorldCoords location) {
        this.text = text;
        this.coords = location;
        this.font = font;
        this.height = 0.05f;
        this.justification = Justify.LEFT;

        this.wrapWidth = -1;
        this.yCutoff = -1;
    }

    /**
     * Sets the width of the text before a linebreak occurs. -1 by default. Set the value to -1 to not have automatic
     * line breaking.
     * <br>
     * If the wrap width is lower than the width of a single character, the character will be added anyway and a newline
     * will be added.
     *
     * @param wrapWidth The width of the text.
     * @return this
     */
    public TextBuilder setWrapWidth(float wrapWidth) {
        this.wrapWidth = wrapWidth;
        return this;
    }

    public float getWrapWidth() {
        return this.wrapWidth;
    }

    /**
     * Sets maximum height of the text before it is cut off. -1 by default. Set the value to -1 to not have a cutoff
     * <br>
     * An error will be thrown when creating the text if yCutoff is less than the text height.
     *
     * @param yCutoff The height of the text.
     * @return this
     */
    public TextBuilder setYCutoff(float yCutoff) {
        this.yCutoff = yCutoff;
        return this;
    }

    public float getYCutoff() {
        return this.yCutoff;
    }

    /**
     * Sets the text to display.
     * @param text The text to display.
     * @return this
     */
    public TextBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return this.text;
    }

    /**
     * Sets the top left coordinate of the text. WorldCoords.getMiddle() by default.
     * @param coords The top left coordinate of the text.
     * @return this
     */
    public TextBuilder setCoords(WorldCoords coords) {
        this.coords = coords;
        return this;
    }

    public WorldCoords getCoords() {
        return this.coords;
    }

    /**
     * Sets the font to use for this text.
     * @param font The font to use for this text.
     * @return this
     */
    public TextBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    public Font getFont() {
        return this.font;
    }

    /**
     * Sets the Y height of each line of text, in WorldCoords. 0.05 by default.
     *
     * @deprecated Use {@link #setHeight(float)} instead.
     * @param yHeight The Y height of each line of text, in WorldCoords.
     * @return this
     */
    @Deprecated
    public TextBuilder setYHeight(float yHeight) {
        this.height = yHeight;
        return this;
    }

    /**
     * Sets the height of a single line of text, in WorldCoords. 0.05 by default.
     *
     * @param height The height of the text line
     * @return this
     */
    public TextBuilder setHeight(float height) {
        this.height = height;
        return this;
    }

    /**
     * @deprecated Use {@link #getHeight()} instead.
     */
    @Deprecated
    public float getYHeight() {
        return this.height;
    }

    /**
     * @return The height of a line of text
     */
    public float getHeight() {
        return this.height;
    }

    /**
     * Sets the justification of the text. Justify.LEFT by default.
     * @param justification The justification of the text.
     * @return this
     */
    public TextBuilder setJustification(Justify justification) {
        this.justification = justification;
        return this;
    }

    public Justify getJustification() {
        return this.justification;
    }

    /**
     * Creates the text object.
     * @return The text object.
     */
    public Text toText() {
        return new Text(this);
    }
}
