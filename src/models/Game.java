package models;

import database.Database;
import database.DatabaseAccessor;
import enumerations.GameState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private int ID;
    private ArrayList<User> players;
    private ArrayList<Message> messages;
    private GameState gameState;

    public Game(int ID) {
        this.ID = ID;
        this.messages = DatabaseAccessor.selectMessages(this);
    }

    public boolean hasPlayer(User user) {
        return players.stream().anyMatch(e -> e.equals(user) );
    }
    public Game(int ID, List<String> players, String state) {
        this.ID = ID;
        this.players = players.stream().map(User::new).collect(Collectors.toCollection(ArrayList<User>::new));
        this.messages = DatabaseAccessor.selectMessages(this);
        this.gameState = GameState.stateFor(state);
    }

    public int getID() {
        return ID;
    }

    public boolean addMessage(User user, String message) {
        if(players.contains(user)){
            DatabaseAccessor.addMessage(user, this, message);
            return true;
        }
        return false;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public static ArrayList<Game> getFor(User user) {
        return DatabaseAccessor.selectGames(user);
    }
    public static ArrayList<Game> getAll() {
        return DatabaseAccessor.selectGames();
    }

    @Override
    public String toString() {
        return "Game{" +
                "ID=" + ID +
                ", players=" + players +
                '}';
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
