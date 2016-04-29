package controllers;


import enumerations.GameState;
import models.Game;
import models.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameController {

    private ArrayList<Game> games;
    private User currentUser;

    public GameController() {
        this.games = Game.getAll();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public ArrayList<Game> getOwnedGames() {
        return games.stream()
                .filter(game -> game.hasPlayer(currentUser))
                .collect(Collectors.toCollection(ArrayList<Game>::new));
    }

    public void refresh() {
        this.games = Game.getAll();
    }
}
