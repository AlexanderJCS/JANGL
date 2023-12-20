package jangl.graphics.font;

import jangl.coords.PixelCoords;
import jangl.coords.WorldCoords;
import jangl.graphics.batching.Batch;
import jangl.graphics.batching.BatchBuilder;
import jangl.graphics.shaders.ShaderProgram;
import jangl.shapes.Transform;

import java.util.Objects;

public class Text implements AutoCloseable {
    private static final float NEWLINE_SPACING = 1.2f;
    private Batch batch;
    private String text;
    private WorldCoords coords;
    private Font font;
    private float yHeight;
    private Justify justification;
    private float yCutoff;
    private float wrapWidth;

    public Text(TextBuilder builder) throws IllegalArgumentException {
        if (builder.getYCutoff() != -1 && builder.getYCutoff() < builder.getYHeight()) {
            throw new IllegalArgumentException("The yCutoff must be greater than the yHeight!");
        }

        this.wrapWidth = builder.getWrapWidth();
        this.yCutoff = builder.getYCutoff();



        this.coords = builder.getCoords();
        this.yHeight = builder.getYHeight();
        this.font = builder.getFont();
        this.text = this.processText(builder.getText());
        this.justification = builder.getJustification();

        this.batch = this.getBatch();
    }

    private String processText(String text) {
        return this.addLineBreaks(this.pruneText(text));
    }

    private String addLineBreaks(String text) {
        StringBuilder builder = new StringBuilder();

        int heightPixels = this.font.tallestLetter.height();
        float heightWorldCoords = PixelCoords.distToWorldCoords(heightPixels);

        // desired height = current height * scale. Solving for scale: scale = desired height / current height
        float scaleFactor = this.yHeight / heightWorldCoords;

        WorldCoords cursor = new WorldCoords(0, 0);
        for (int i = 0; i < text.length(); i++) {
            // Stop if the cursor is above the cutoff
            if (this.yCutoff != -1 && cursor.y > this.yCutoff) {
                break;
            }

            char ch = text.charAt(i);

            // Check if a newline is manually given
            if (ch == '\n') {
                cursor.x = 0;
                cursor.y += this.yHeight * NEWLINE_SPACING;
                builder.append(ch);
                continue;
            }

            // Advance the cursor and check if a newline is needed
            CharInfo info = this.font.getInfo(ch);
            cursor.x += PixelCoords.distToWorldCoords(info.xAdvance()) * scaleFactor;

            if (this.wrapWidth != -1 && cursor.x > this.wrapWidth) {
                // If this is the first character in the line, just add it and move on
                // This prevents an infinite loop
                if (Float.compare(cursor.x, info.xAdvance() * scaleFactor) == 0) {
                    builder.append(ch);
                    i++;  // counteract the i-- below
                }

                builder.append('\n');

                cursor.x = 0;
                cursor.y += this.yHeight * NEWLINE_SPACING;

                i--;  // go back 1 character so this current character can be processed again
                continue;
            }

            // Append the character if nothing else goes wrong
            builder.append(ch);
        }

        return builder.toString();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText) {
        String processed = this.processText(newText);

        // Don't regenerate if the text is the same
        if (processed.equals(this.text)) {
            return;
        }

        this.text = processed;
        this.regenerate();
    }

    /**
     * Removes any chars that cannot be displayed from the string.
     *
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

    private void addCharacter(BatchBuilder builder, PixelCoords cursor, CharInfo info, float scaleFactor) {
        final int[] charIndices = new int[]{
                0, 1, 2,
                2, 3, 0
        };

        WorldCoords scCursor = cursor.toWorldCoords();

        // x1 = left, x2 = right
        // y1 = top, y2 = bottom
        float x1 = scCursor.x;
        float y1 = scCursor.y;
        float x2 = scCursor.x + PixelCoords.distToWorldCoords(info.width()) * scaleFactor;
        float y2 = scCursor.y - PixelCoords.distToWorldCoords(info.height()) * scaleFactor;

        float[] charVertices = new float[]{
                x1, y1,
                x2, y1,
                x2, y2,
                x1, y2
        };

        float[] charTexCoords = this.font.getTexCoords((char) info.charID());

        builder.addObject(charVertices, charIndices, charTexCoords);
    }

    private void generateLineRightJustify(BatchBuilder builder, PixelCoords cursor, String text, float scaleFactor) {
        for (int i = text.length() - 1; i >= 0; i--) {
            char ch = text.charAt(i);
            CharInfo info = this.font.getInfo(ch);

            if (info == null) {
                continue;
            }

            cursor.x -= info.xAdvance() * scaleFactor;

            cursor.x += info.xOffset() * scaleFactor;
            cursor.y -= info.yOffset() * scaleFactor;

            this.addCharacter(builder, cursor, info, scaleFactor);

            cursor.x -= info.xOffset() * scaleFactor;
            cursor.y += info.yOffset() * scaleFactor;
        }
    }

    private void generateLineCenterJustify(BatchBuilder builder, PixelCoords cursor, String text, float scaleFactor) {
        int lineLength = 0;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            CharInfo info = this.font.getInfo(ch);

            if (info == null) {
                continue;
            }

            lineLength += (int) (info.xAdvance() * scaleFactor);
        }

        float halfLength = lineLength / 2f;
        cursor.x -= halfLength;

        generateLineLeftJustify(builder, cursor, text, scaleFactor);
    }

    private void generateLineLeftJustify(BatchBuilder builder, PixelCoords cursor, String text, float scaleFactor) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            CharInfo info = this.font.getInfo(ch);

            if (info == null) {
                continue;
            }

            cursor.x += info.xOffset() * scaleFactor;
            cursor.y -= info.yOffset() * scaleFactor;

            this.addCharacter(builder, cursor, info, scaleFactor);

            cursor.x -= info.xOffset() * scaleFactor;
            cursor.y += info.yOffset() * scaleFactor;

            cursor.x += info.xAdvance() * scaleFactor;
        }
    }

    /**
     * @throws NullPointerException If this.justification is null
     */
    private void generateNextLine(BatchBuilder builder, PixelCoords cursor, String text, float scaleFactor) throws NullPointerException {
        Objects.requireNonNull(this.justification, "Justification must not be null");

        if (this.justification == Justify.LEFT) {
            this.generateLineLeftJustify(builder, cursor, text, scaleFactor);
        } else if (this.justification == Justify.RIGHT) {
            this.generateLineRightJustify(builder, cursor, text, scaleFactor);
        } else if (this.justification == Justify.CENTER) {
            generateLineCenterJustify(builder, cursor, text, scaleFactor);
        }
    }

    /**
     * @throws NullPointerException If this.justification is null
     */
    private Batch getBatch() throws NullPointerException {
        int heightPixels = this.font.tallestLetter.height();
        float heightWorldCoords = PixelCoords.distToWorldCoords(heightPixels);

        // desired height = current height * scale. Solving for scale: scale = desired height / current height
        float scaleFactor = this.yHeight / heightWorldCoords;

        // The cursor is where the next char should be drawn
        PixelCoords cursor = this.coords.toPixelCoords();

        BatchBuilder builder = new BatchBuilder();

        for (String line : this.text.split("\n")) {
            this.generateNextLine(builder, cursor, line, scaleFactor);

            // Reset cursor position
            cursor.x = this.coords.toPixelCoords().x;
            cursor.y -= WorldCoords.distToPixelCoords(this.yHeight) * NEWLINE_SPACING;
        }

        return new Batch(builder);
    }

    /**
     * Regenerate the new model with any changes that may have been made since the last time it was generated
     */
    protected void regenerate() {
        this.batch.close();  // close the old model before generating the new one
        this.batch = this.getBatch();
    }

    /**
     * @return A copy of the top left coordinates
     */
    public WorldCoords getCoords() {
        return new WorldCoords(this.coords.x, this.coords.y);
    }

    public void setCoords(WorldCoords newCoords) {
        this.coords = new WorldCoords(newCoords.x, newCoords.y);
        this.regenerate();
    }

    public Justify getJustification() {
        return this.justification;
    }

    public void setJustification(Justify newJustification) {
        this.justification = newJustification;
        this.regenerate();
    }

    public float getYCutoff() {
        return yCutoff;
    }

    public void setYCutoff(float yCutoff) {
        this.yCutoff = yCutoff;
        this.regenerate();
    }

    public float getWrapWidth() {
        return wrapWidth;
    }

    public void setWrapWidth(float wrapWidth) {
        this.wrapWidth = wrapWidth;
        this.regenerate();
    }

    /**
     * @return The font object being used.
     */
    public Font getFont() {
        return font;
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

    /**
     * @return The y height, in world coords, of the tallest letter in the font.
     * Other letters are proportional.
     */
    public float getYHeight() {
        return yHeight;
    }

    public void setYHeight(float newYHeight) {
        this.yHeight = newYHeight;
        this.regenerate();
    }

    /**
     * Changes the text's position by a specified amount.
     *
     * @param x The x delta to move.
     * @param y The y delta to move.
     */
    public void shift(float x, float y) {
        this.batch.getTransform().shift(x, y);
    }

    /**
     * Changes the text's position by a specified amount.
     *
     * @param shiftCoords The amount to shift the object by.
     */
    public void shift(WorldCoords shiftCoords) {
        this.batch.getTransform().shift(shiftCoords);
    }

    public Transform getTransform() {
        return this.batch.getTransform();
    }

    public void draw() {
        ShaderProgram shaderProgram = this.font.getShaderProgram();

        shaderProgram.bind();
        this.font.fontTexture.bind();
        this.batch.draw();
        this.font.fontTexture.unbind();
        shaderProgram.unbind();
    }

    @Override
    public void close() {
        this.batch.close();
    }
}
