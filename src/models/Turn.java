package models;

import enumerations.TurnType;

import java.util.ArrayList;


public class Turn {
    private int id;
    private int gameId;
    private int score;
    private User user;
    private TurnType type;
    private ArrayList<Tile> tiles;

    public Turn(int id,int gameId, int score, User user, TurnType type, ArrayList<Tile> tiles) {
        this.id = id;
        this.gameId = gameId;
        this.score = score;
        this.user = user;
        this.type = type;
        this.tiles = tiles;
    }

    public Turn(int id, int gameId, int score, User user, TurnType type) {
        this.gameId = gameId;
        this.id = id;
        this.score = score;
        this.user = user;
        this.type = type;
    }

    public String getWord() {
        //TODO get the complete word, not just the tiles added to an existing word
        return tiles.toString();
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public int getId() {
        return id;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public String toString() {
        if(tiles != null)
            return user + " speelde " + getWord() + " voor " + score + " punten.";
        return user + " deed " + type;
    }
}
