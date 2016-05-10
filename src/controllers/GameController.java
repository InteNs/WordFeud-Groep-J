package controllers;

import database.access.GameDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Game;

import java.util.ArrayList;

public class GameController extends Controller {

    private ArrayList<Game> games;
    private ObjectProperty<Game> selectedGame = new SimpleObjectProperty<>();

    public GameController(ControllerFactory factory) {
        super(factory);
    }

    public ObjectProperty<Game> selectedGameProperty() {
        return selectedGame;
    }

    public Game getSelectedGame() {
        return selectedGame.get();
    }

    public void setSelectedGamee(Game game) {
        selectedGame.set(game);
    }

    public ObservableList<Game> getGames() {
        return FXCollections.observableArrayList(games);
    }

    public void refresh() {
        this.games = GameDAO.selectGames();
    }
}
