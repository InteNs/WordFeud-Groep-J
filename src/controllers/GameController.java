package controllers;

import database.access.GameDAO;
import enumerations.GameState;
import models.Game;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameController extends Controller {

    private ArrayList<Game> games;

    public GameController() {
        this.games = GameDAO.selectGames();
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
}
