package controllers;

import database.access.GameDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Game;

public class GameController extends Controller {

    private ObservableList<Game> games;
    private ObjectProperty<Game> selectedGame;

    public GameController(ControllerFactory factory) {
        super(factory);
        games = FXCollections.observableArrayList(GameDAO.selectGames());
        selectedGame = new SimpleObjectProperty<>();
    }

    public ObjectProperty<Game> selectedGameProperty() {
        return selectedGame;
    }

    public Game getSelectedGame() {
        return selectedGame.get();
    }

    public void setSelectedGame(Game game) {
        selectedGame.set(game);
    }

    public ObservableList<Game> getGames() {
        return games;
    }

    public void refresh() {
        games.setAll(GameDAO.selectGames());
    }
}
