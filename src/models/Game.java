package models;

import database.access.GameDAO;
import enumerations.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

    private int ID;
    private ArrayList<User> players;
    private ArrayList<Message> messages;
    private GameState gameState;
    private User opponent;

    public Game(int ID) {
        this.ID = ID;
    }

    public Game(int ID, List<String> players, String state) {
        this.ID = ID;
        this.players = players.stream().limit(2).map(User::new).collect(Collectors.toCollection(ArrayList<User>::new));
        this.gameState = GameState.stateFor(state);
    }

    public int getID() {
        return ID;
    }

    /**
     * flags the opponent of this game
     *
     * @param currentUser the user that is not the opponent
     * @return the opponent
     */
    public User flagOpponent(User currentUser) {
        if (players.contains(currentUser))
            opponent = players.stream().filter(user -> !user.equals(currentUser)).collect(Collectors.toList()).get(0);
        return opponent;
    }

    public ArrayList<Message> getMessages(boolean refresh) {
        if (refresh) this.messages = GameDAO.selectMessages(this);
        return messages;
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

    public String toString() {
        if (opponent != null)
            return "[" + ID + "] spel tegen  " + opponent.getName();
        else
            return "[" + ID + "] spel tussen " + players.get(0) + " en " + players.get(1);
    }
}
