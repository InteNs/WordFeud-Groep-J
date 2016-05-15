package views.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Field;
import models.Tile;
import resources.Resource;

public class FieldTileNode extends ImageView {

    private Field field;
    private Tile tile;
    private Resource resource;
    private boolean isPlaceHolder = false;

    public Field getField() {
        return field;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
        isPlaceHolder = tile == null;
        redrawImage();
    }

    /**
     * Used to create nodes representing the gameBoard
     */
    public FieldTileNode(Field field, Resource resource) {
        this.resource = resource;
        this.setFitWidth(40);
        this.setFitHeight(40);
        this.setImage(getImage(field));
        this.field = field;

    }

    /**
     * Used to build the playerRack
     */
    public FieldTileNode(Tile tile, Resource resource) {
        this.setFitHeight(80);
        this.setFitWidth(80);
        this.resource = resource;
        this.tile = tile;
        if (tile != null)
            this.setImage(getImage(tile));
        else this.isPlaceHolder = true;
    }

    public void redrawImage() {
        if (isPlaceHolder) {
            this.setImage(new Image("resources/blank.png"));
            this.setOpacity(0);
        } else {
            this.setOpacity(1);
            if (tile != null) this.setImage(getImage(tile));
            if (field != null) this.setImage(getImage(field));
        }
    }

    private Image getImage(Field field) {
        String s;
        if (field.getTile() == null)
            s = field.getFieldType().toString();
        else
            s = field.getTile().toString().toUpperCase();

        String myString = s + ".png";
        return resource.getImage(myString);
    }

    private Image getImage(Tile tile) {
        String s;
        s = tile.toString().toUpperCase();
        String myString = s + ".png";
        return resource.getImage(myString);
    }

    public boolean isPlaceHolder() {
        return isPlaceHolder;
    }
}
