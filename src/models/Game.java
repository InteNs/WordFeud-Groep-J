package models;

import database.access.GameDAO;
import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Game {

    private int id;
    //These UserObjects are different instances then the UserObjects created in the controllers;
    private ArrayList<Message> messages;
    private GameState gameState;
    private User opponent;
    private User challenger;
    private Language language;
    private BoardType boardType;
    private ArrayList<Turn> turns;

    public Game(int id, User challenger, User opponent, GameState state, BoardType boardType, Language language) {
        this.id = id;
        this.challenger = challenger;
        this.opponent = opponent;
        this.gameState = state;
        this.language = language;
        this.boardType = boardType;
    }

    public int getId() {
        return id;
    }

    public int setMessages(ArrayList<Message> messages) {
        int diff = 0;
        if(this.messages != null) diff = messages.size() - this.messages.size();
        this.messages = messages;
        return diff;
    }

    public int setTurns(ArrayList<Turn> turns) {
        int diff = 0;
        if(this.turns != null) diff = turns.size() - this.turns.size();
        this.turns = turns;
        return diff;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public ArrayList<Tile> getPlacedTiles() {
        ArrayList<Tile> tiles = new ArrayList<>();
        if(turns != null)
            turns.stream().map(Turn::getTiles).forEach(tiles::addAll);
        return tiles;
    }

    public ArrayList<User> getPlayers() {
        return new ArrayList<>(Arrays.asList(new User[]{challenger, opponent}));
    }

    public boolean hasPlayer(User user) {
        return getPlayers().contains(user);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public String toString() {
        return "[" + id + "]["+language+"] " +boardType.toString().toLowerCase()
                +" spel tussen " + challenger + " en " + opponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return id == game.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
