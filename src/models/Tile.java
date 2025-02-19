package models;

public class Tile {

    private int value;
    private Character character;
    private Character replacedJokerCharacter;
    private Integer x;
    private Integer y;
    private int id;

    public Tile(Character character) {
        this.character = character;
    }

    public Tile(char character, int value) {
        this.character = character;
        this.value = value;
    }

    public Tile(int id, int value, Character character) {
        this.id = id;
        this.value = value;
        this.character = character;
    }

    public Tile(char character, int x, int y) {
        this.character = character;
        this.x = x;
        this.y = y;
    }

    public Character getReplacedJokerCharacter() {
        return replacedJokerCharacter;
    }

    public int getValue(){
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Character getCharacter() {
        return character;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isJokerTile() {
        return character == '?';
    }

    @Override
    public String toString() {
        if (isJokerTile())
            if (replacedJokerCharacter == null) {
                return "blank"; // Placeholder value for Joker
            } else {
                return replacedJokerCharacter.toString();
            }
        return character.toString();
    }

    public void replaceJoker(Character choice) {
        replacedJokerCharacter = choice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;

        return id == tile.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public void clearCoordinates() {
        x = null;
        y = null;
    }

    public String getReplacedJokerAsString() {
        if (replacedJokerCharacter == null) return null;
        return replacedJokerCharacter.toString();
    }
}
