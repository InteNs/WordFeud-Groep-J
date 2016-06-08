package models;

import enumerations.TurnType;

import java.util.ArrayList;


public class Turn {
    private int gameId;
    private int id;
    private int amountSwapped;
    private int score;
    private String word;
    private User user;
    private TurnType type;
    private ArrayList<Tile> rack;
    private ArrayList<Tile> placedTiles;

    public Turn(int gameId, int id, int score, User user, TurnType type) {
        this(id, score, user, type);
        this.gameId = gameId;
    }

    public Turn(int id, int score, User user, TurnType type) {
        this.id = id;
        this.score = score;
        this.user = user;
        this.type = type;
        this.rack = new ArrayList<>();
        this.placedTiles = new ArrayList<>();
    }

    public Turn(int id, int score, User user, TurnType type, ArrayList<Tile> placedTiles, ArrayList<Tile> playerRack) {
        this(id, score, user, type);
        this.rack = playerRack;
        this.placedTiles = placedTiles;
    }

    public void setAmountSwapped(int amountSwapped) {
        this.amountSwapped = amountSwapped;
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

    public void setType(TurnType type) {
        this.type = type;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<Tile> getPlacedTiles() {
        if (placedTiles == null)
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
        switch (type) {
            case BEGIN:
                return "";
            case END:
                if (score == 0)
                    return user + " heeft geen punten aftrek gekregen";
                if (score < 0)
                    return user + " heeft " + Math.abs(score) + " punten aftrek gekregen";
                if (score > 0)
                    return user + " heeft " + score + " punten erbij gekregen";
            case PASS:
                return user + " heeft gepast ";
            case RESIGN:
                return user + " heeft opgegeven ";
            case SWAP:
                return user + " heeft " + amountSwapped + " letters geruild";
            case WORD:
                return user + " heeft " + word + " gespeeld voor " + score + " punten";
            default:
                return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Turn turn = (Turn) o;

        return id == turn.id && gameId == turn.gameId;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
