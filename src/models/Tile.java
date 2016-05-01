package models;

import javafx.util.Pair;

public class Tile {

    private int value;
    private Character character;
    private int x;
    private int y;

    public Tile(int value, Character character) {
        this.value = value;
        this.character= character;
    }

    public Tile(char character, int x, int y) {
        this.character = character;
        this.x = x;
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

    @Override
    public String toString() {
        return character.toString();
    }
}
