package controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;
import java.util.List;

public class GameController extends Controller {

    private ObservableList<Game> games;
    private ObjectProperty<Game> selectedGame;
    private ObjectProperty<Turn> selectedTurn;

    public GameController(ControllerFactory factory) {
        super(factory);
        games = FXCollections.observableArrayList();
        selectedGame = new SimpleObjectProperty<>();
        selectedTurn = new SimpleObjectProperty<>();
        selectedGameProperty().addListener((o, oV, nV) -> loadGame(nV));
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

    public Turn getSelectedTurn() {
        return selectedTurn.get();
    }

    public void setSelectedTurn(Turn selectedTurn) {
        this.selectedTurn.set(selectedTurn);
    }

    public ObjectProperty<Turn> selectedTurnProperty() {
        return selectedTurn;
    }

    public ObservableList<Game> getGames() {
        return games;
    }

    public void loadGame(Game game) {
        game.setBoard(gameDAO.selectFieldsForBoard(game.getBoardType()));
        game.setTurns(gameDAO.selectTurns(game));
        game.setMessages(gameDAO.selectMessages(game));
        game.setPot(gameDAO.selectPot(game.getLanguage()));
    }

    public void refresh() {
        games.setAll(gameDAO.selectGames());
    }

    public void setPlayerRack(Game game, List<Tile> tiles) {
        game.getCurrentRack().setAll(tiles);
    }

    public void setBoardState(Game game, Turn turn) {
        game.setBoardStateTo(turn);
    }

    public void placeTile(Game game, Field field, Tile tile) {
        game.addPlacedTile(field, tile);
    }

    public void removeTile(Game game, Field field) {
        game.removePlacedTile(field);
    }

    public void sendMessage(Game game, User user, String text) {
        game.sendMessage(user, text);
        gameDAO.insertMessage(game, user, text);
    }

    public boolean isJokerTile(Tile tile) {
        if (tile.toString().equals("blank")) {
            return true;
        }return false;
    }
}
