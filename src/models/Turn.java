package models;

import enumerations.TurnType;

import java.util.ArrayList;


public class Turn {
    private int id;
    private int score;
    private String woord;
    private User user;
    private TurnType type;
    private ArrayList<Tile> rack;
    private ArrayList<Tile> placedTiles;

    public Turn(int id, int score, User user, TurnType type) {
        this.id = id;
        this.score = score;
        this.user = user;
        this.type = type;
        rack = new ArrayList<>();
        placedTiles = new ArrayList<>();
    }

    public Turn(int id, int score, User user, TurnType type, ArrayList<Tile> placedTiles, ArrayList<Tile> playerRack){
        this.id = id;
        this.score = score;
        this.user = user;
        this.type = type;
        rack = playerRack;
        this.placedTiles = placedTiles;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public User getUser() {
        return user;
    }

    public TurnType getType() {
        return type;
    }

    public void addPlacedTile(Tile tile) {
        placedTiles.add(tile);
    }

    public void addRackTile(Tile tile) {
        rack.add(tile);
    }

    public boolean hasRackTile(Tile tile) {
        return rack.contains(tile);
    }

    public boolean hasPlacedTile(Tile tile) {
        return placedTiles.contains(tile);
    }

    public String getWoord() {
        return woord;
    }

    public void setWoord(String woord) {
        this.woord = woord;
    }

    public ArrayList<Tile> getPlacedTiles() {
        if (placedTiles==null)
            return new ArrayList<Tile>();

        return placedTiles;
    }

    public ArrayList<Tile> getRack() {
        if (rack != null) {
            return rack;
        } else {
            return new ArrayList<>();
        }
    }
    @Override
    public String toString() {
        return "Speler: "+user+" -Actie:"+type+" -Beurtscore:"+score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Turn turn = (Turn) o;

        return id == turn.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
