package views.components;

import javafx.animation.FadeTransition;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import models.Field;
import models.Tile;
import resources.ResourceFactory;

public class FieldTileNode extends ImageView {

    private Field field;
    private Tile tile;
    private ResourceFactory resourceFactory;
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
    public FieldTileNode(Field field, ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        this.setFitWidth(40);
        this.setFitHeight(40);
        this.setImage(getImage(field));
        this.field = field;

    }

    /**
     * Used to build the playerRack
     */
    public FieldTileNode(Tile tile, ResourceFactory resourceFactory) {
        this.setFitHeight(80);
        this.setFitWidth(80);
        this.resourceFactory = resourceFactory;
        this.tile = tile;
        if (tile != null)
            this.setImage(getImage(tile));
        else this.isPlaceHolder = true;
    }

    public void redrawImage() {
        if (isPlaceHolder) {
            this.setImage(getImage("blank.png"));
            this.setOpacity(0);
        } else {
            this.setOpacity(1);
            if (tile != null) this.setImage(getImage(tile));
            if (field != null) this.setImage(getImage(field));
        }
    }

    private Image getImage(String image) {
        return resourceFactory.getImage(image, true);
    }

    private Image getImage(Field field) {
        String s;
        if (field.getTile() == null)
            s = field.getFieldType().toString();
        else
            s = field.getTile().toString().toUpperCase();

        String myString = s + ".png";
        return resourceFactory.getImage(myString, false);
    }

    private Image getImage(Tile tile) {
        String s;
        s = tile.toString().toUpperCase();
        String myString = s + ".png";
        return resourceFactory.getImage(myString, true);
    }

    public boolean isPlaceHolder() {
        return isPlaceHolder;
    }

    @Override
    public String toString() {
        return "FieldTileNode{" +
                "field=" + field +
                ", tile=" + tile +
                '}';
    }

    public void highLight (){
        this.setEffect(new SepiaTone(1));
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(10);
        ft.setAutoReverse(true);
        ft.play();
    }
}
