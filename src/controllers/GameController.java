package controllers;

import database.access.GameDAO;
import enumerations.BoardType;
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

    public void loadGame(Game game) {
        game.setMessages(GameDAO.selectMessages(game));
        game.setTurns(GameDAO.selectTurns(game));
        game.setBoard(GameDAO.selectFieldsForBoard(game.getBoardType()));
    }

    public void loadGameBoard(Game game){
    }
}
