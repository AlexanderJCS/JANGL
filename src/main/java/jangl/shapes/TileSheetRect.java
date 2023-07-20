package jangl.shapes;

import jangl.coords.WorldCoords;
import jangl.graphics.models.TexturedModel;


public class TileSheetRect extends Rect {
    private final int tilesWidth, tilesHeight;
    private int tileX, tileY;

    /**
     * @param topLeft     the top left coordinate of the rect
     * @param width       The width of the rect, units of world coordinates
     * @param height      The height of the rect, units of world coordinates
     * @param tilesWidth  The number of tiles on the x-axis of the texture.
     * @param tilesHeight The number of tiles on the y-axis of the texture.
     */
    public TileSheetRect(WorldCoords topLeft, float width, float height, int tilesWidth, int tilesHeight) {
        super(topLeft, width, height);

        this.tilesWidth = tilesWidth;
        this.tilesHeight = tilesHeight;

        this.setTilePos(1, 1);
    }

    /**
     * Set the tile you want to display within the tile sheet. Provide a zero-indexed x and y coordinate.
     *
     * @param newX The zero-indexed x coordinate of the tile to display.
     * @param newY The zero-indexed y coordinate of the tile to display.
     */
    public void setTilePos(int newX, int newY) {
        if (newX < 0 || newX >= this.tilesWidth || newY < 0 || newY >= this.tilesHeight) {
            throw new IllegalArgumentException(
                    "The new x or new y tile coordinate is out of bounds. The x must be between the range [0, tilesWidth)" +
                            " and the y must be between the range [0, tilesHeight)"
            );
        }

        this.tileX = newX;
        this.tileY = newY;

        TexturedModel texturedModel = (TexturedModel) this.model;
        texturedModel.subTexCoords(this.getTexCoords(), 0);
    }

    @Override
    public float[] getTexCoords() {
        float leftX = (float) this.tileX / this.tilesWidth;
        float rightX = leftX + 1f / this.tilesWidth;
        float topY = (float) this.tileY / this.tilesHeight;
        float bottomY = topY + 1f / this.tilesHeight;

        return new float[]{
                leftX, topY,
                rightX, topY,
                rightX, bottomY,
                leftX, bottomY
        };
    }
}
