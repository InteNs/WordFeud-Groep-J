package controllers;

import database.access.GameDAO;
import enumerations.GameState;
import models.Game;
import models.User;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameController {

    private ArrayList<Game> games;
    private User currentUser;

    public GameController() {
        this.games = GameDAO.selectGames();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public ArrayList<Game> getGames() {
        if (currentUser != null)
            return games;
        return new ArrayList<>();
    }

    public ArrayList<Game> getGames(GameState state) {
        return getGames().stream().filter(game -> game.getGameState() == state).collect(Collectors.toCollection(ArrayList<Game>::new));
    }

    public ArrayList<Game> getOwnedGames() {
        return getGames().stream()
                .filter(game -> game.hasPlayer(currentUser))
                .collect(Collectors.toCollection(ArrayList<Game>::new));
    }

    public ArrayList<Game> getOwnedGames(GameState state) {
        return getOwnedGames().stream()
                .filter(game -> game.getGameState() == state)
                .collect(Collectors.toCollection(ArrayList<Game>::new));
    }

    public void refresh() {
        this.games = GameDAO.selectGames();
    }

    public void loadGame(Game game) {
        game.setMessages(GameDAO.selectMessages(game));
        game.setTurns(GameDAO.selectTurns(game));
        game.getTurns().forEach(System.out::println);
    }

    public void loadGameBoard(Game game){
        game.setBoard(GameDAO.selectFieldsForBoard(game.getBoardType()));
    }
}
