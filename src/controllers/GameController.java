package controllers;

import database.access.GameDAO;
import enumerations.GameState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Game;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameController extends Controller {

    private ArrayList<Game> games;

    public GameController() {
        this.games = GameDAO.selectGames();
    }

    public ObservableList<Game> getGames() {
        return FXCollections.observableArrayList(games);
    }

    public void refresh() {
        this.games = GameDAO.selectGames();
    }
}
