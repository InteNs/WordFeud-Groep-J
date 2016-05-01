package models;

import enumerations.TurnType;

import java.util.ArrayList;


public class Turn {
    private int score;
    private String woord;
    private User user;
    private TurnType type;
    private ArrayList<Tile> rack;
    private ArrayList<Tile> placedTiles;

    public Turn(int score, User user, TurnType type, ArrayList<Tile> placedTiles, ArrayList<Tile> rack) {
        this.score = score;
        this.user = user;
        this.type = type;
        this.placedTiles = placedTiles;
        this.rack = rack;
    }

    public String getWoord() {
        return woord;
    }

    public void setWoord(String woord) {
        this.woord = woord;
    }

    public ArrayList<Tile> getPlacedTiles() {
        return placedTiles;
    }

    public ArrayList<Tile> getRack() {
        return rack;
    }
}
