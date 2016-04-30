package models;

import database.DatabaseAccessor;
import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private int ID;

    //These UserObjects are different instances then the UserObjects created in the controllers;
    private ArrayList<User> players;

    private ArrayList<Message> messages;
    private GameState gameState;
    private User opponent;
    private Language language;
    private BoardType boardType;
    private ArrayList<Letter> potOfLetters;

    public Game(int ID) {
        this.ID = ID;
    }

    public Game(int ID, List<String> players, String state,String boardType, String language) {
        this.ID = ID;
        this.players = players.stream().map(User::new).collect(Collectors.toCollection(ArrayList<User>::new));
        this.gameState = GameState.stateFor(state);
        this.language=Language.languageFor(language);
        this.boardType=BoardType.boardTypeFor(boardType);
        potOfLetters = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public User getOpponent(User currentUser) {
        if(opponent == null)
            this.opponent = players.stream().filter(user -> !user.equals(currentUser)).findFirst().get();
        return opponent;
    }

    public ArrayList<Message> getMessages(boolean refresh) {
        if(refresh) this.messages = DatabaseAccessor.selectMessages(this);
        return messages;
    }

    public boolean addMessage(User user, String message) {
        if (players.contains(user)) {
            DatabaseAccessor.addMessage(user, this, message);
            return true;
        }
        return false;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public boolean hasPlayer(User user) {
        return players.stream().anyMatch(e -> e.equals(user));
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public static ArrayList<Game> getFor(User user) {
        return DatabaseAccessor.selectGames(user);
    }

    public static ArrayList<Game> getAll() {
        return DatabaseAccessor.selectGames();
    }

    public void fillPot(Language language){
        potOfLetters=DatabaseAccessor.SelectLetters(language);
    }


}
