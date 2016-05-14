package views.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Field;
import models.Tile;

public class FieldTileNode extends ImageView {

    private Field field;
    private Tile tile;

    public Field getField() {
        return field;
    }

    public Tile getTile() {
        return tile;
    }

    /**
     * Used to create nodes representing the gameBoard
     */
    public FieldTileNode(Field field) {
        this.setImage(getImage(field));
        this.field = field;
    }

    /**
     * Used to build the playerRack
     */
    public FieldTileNode(Tile tile) {
        if (tile.getCharacter() == null) {
            this.setFitHeight(80);
            this.setFitWidth(80);
        } else {
            this.setImage(getImage(tile));
            this.tile = tile;
        }
    }

    public void redrawImage() {
        this.setImage(getImage(field));
    }

    private Image getImage(Field field) {
        String s;
        if (field.getTile() == null)
            s = field.getFieldType().toString();
        else
            s = field.getTile().toString().toUpperCase();

        String myString = "resources/" + s + ".png";
        return new Image(myString, 40, 40, true, true, true);
    }

    private Image getImage(Tile tile) {
        String s;
        s = tile.toString().toUpperCase();
        String myString = "resources/" + s + ".png";
        return new Image(myString, 80, 80, true, true, true);
    }
}
