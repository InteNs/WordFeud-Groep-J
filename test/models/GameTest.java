package models;

import controllers.GameController;
import database.access.GameDAO;

import static org.junit.Assert.*;

public class GameTest {
    @org.junit.Test
    public void checkRow() throws Exception {
        Game game= GameDAO.selectGames().get(2);
        GameController gameController = new GameController();
        gameController.loadGame(game);

    }

}