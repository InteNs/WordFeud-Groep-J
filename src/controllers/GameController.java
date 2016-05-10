package controllers;

import database.access.GameDAO;
import enumerations.BoardType;
import enumerations.GameState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Game;
import models.Tile;
import models.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameController extends Controller {

    private ArrayList<Game> games;

    public GameController() {
        refresh();
    }

    public ObservableList<Game> getGames() {
        return FXCollections.observableArrayList(games);
    }

    public void refresh() {
        this.games = GameDAO.selectGames();
        games.forEach(game -> game.setMessages(GameDAO.selectMessages(game)));
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
}
