package models;

import enumerations.FieldType;

public class Field {

    private Tile tile;
    private FieldType fieldType;
    private int x;
    private int y;

    public Tile getTile() {
        return tile;
    }

    public Field(FieldType fieldType, int x, int y) {

        this.fieldType = fieldType;
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public boolean containsJoker() {
        return tile != null && tile.isJokerTile();
    }

    @Override
    public String toString() {
       if (tile==null)
           return "-";
        else
           return tile.toString();
    }
}
