package models;

public class Tile {

    private int value;
    private Character character;
    private Character replacedJokerCharacter = '?';
    private int x;
    private int y;

    public Tile(Character character) {
        this.character = character;
    }

    public Tile(int value, Character character) {
        this.value = value;
        this.character = character;
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
        if (character == '?')
            if (replacedJokerCharacter == '?') {
                return "blank"; // Placeholder value for Joker
            } else {
                return replacedJokerCharacter.toString();
            }
        return character.toString();
    }

    public void replaceJoker(char choice) {
        replacedJokerCharacter = choice;
    }
}
