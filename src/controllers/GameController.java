package controllers;

import database.access.GameDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Game;
import models.Tile;
import models.User;

public class GameController extends Controller {

    private ObservableList<Game> games;
    private ObjectProperty<Game> selectedGame;

    public GameController(ControllerFactory factory) {
        super(factory);
        selectedGame = new SimpleObjectProperty<>();
        games = FXCollections.observableArrayList(GameDAO.selectGames());
        games.forEach(game -> game.setMessages(GameDAO.selectMessages(game)));
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

    public void loadGame(Game game) {
        game.setBoard(GameDAO.selectFieldsForBoard(game.getBoardType()));
        game.setTurns(GameDAO.selectTurns(game));
    }

    public void loadGameBoard(Game game){
    }

    public Tile getTile(Game selectedGame,String hashcode) {
        for (Tile tile:selectedGame.getAllTiles()
                ) {
            if (tile.hashCode()==Integer.valueOf(hashcode)){
                return tile;
            }

        }
        return null;
    }

    public void refresh() {
        games.setAll(GameDAO.selectGames());
        games.forEach(game -> game.setMessages(GameDAO.selectMessages(game)));

    }
}
