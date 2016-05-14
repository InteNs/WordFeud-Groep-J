package models;

import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    private int ID;
    //These UserObjects are different instances then the UserObjects created in the controllers;
    private ArrayList<Message> messages;
    private GameState gameState;
    private User opponent;
    private User challenger;
    private Language language;
    private BoardType boardType;

    public Game(int ID, User challenger, User opponent, GameState state, BoardType boardType, Language language) {
        this.ID = ID;
        this.challenger = challenger;
        this.opponent = opponent;
        this.gameState = state;
        this.language = language;
        this.boardType = boardType;
    }

    public int getID() {
        return ID;
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

    public String toString() {
        return "[" + ID + "]["+language+"] " +boardType.toString().toLowerCase()
                +" spel tussen " + challenger + " en " + opponent;
    }
}
