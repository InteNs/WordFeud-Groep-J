package models;

import enumerations.FieldType;

public class Field {

    private Tile tile;
    private FieldType fieldType;

    public Tile getTile() {
        return tile;
    }

    public Field(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public String toString() {
       if (tile==null)
           return "-";
        else
           return tile.toString();
    }
}
