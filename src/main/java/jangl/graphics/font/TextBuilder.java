package jangl.graphics.font;

import jangl.coords.WorldCoords;

public class TextBuilder {
    private String text;
    private WorldCoords coords;
    private Font font;
    private float yHeight;
    private Justify justification;

    public TextBuilder(Font font, String text) {
        this.text = text;
        this.coords = WorldCoords.getMiddle();
        this.font = font;
        this.yHeight = 0.05f;
        this.justification = Justify.LEFT;
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
     * @param yHeight The Y height of each line of text, in WorldCoords.
     * @return this
     */
    public TextBuilder setYHeight(float yHeight) {
        this.yHeight = yHeight;
        return this;
    }

    public float getYHeight() {
        return this.yHeight;
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
