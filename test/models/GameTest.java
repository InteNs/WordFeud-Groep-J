package models;

import controllers.GameController;
import database.access.GameDAO;
import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;
    private User user1, user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User("current");
        user2 = new User("other");

        game = new Game(511, user1, user2, GameState.FINISHED, BoardType.STANDARD, Language.NL);
    }

    @Test
    public void hasPlayer() throws Exception {
        assertTrue("returns true if game contains given player",
                game.hasPlayer(user1));
        assertTrue("returns true if game contains player with equal name",
                game.hasPlayer(new User(user1.getName())));
    }

    @Test
    public void getPlayers() throws Exception {
        assertTrue("returns all players",
                game.getPlayers().containsAll(Arrays.asList(user1, user2)));
    }

    @Test
    public void checkRow() throws Exception {
        GameController gameController = new GameController();
        gameController.loadGame(game);
        game.setBoardStateTo(game.getTurns().get(game.getTurns().size()-1));
    }
}