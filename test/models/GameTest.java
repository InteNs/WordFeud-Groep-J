package models;

import controllers.ControllerFactory;
import controllers.GameController;
import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;
import enumerations.Role;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class GameTest {

    private Game game;
    private User user1, user2;

    @Before
    public void setUp() throws Exception {
        user1 = new User("current");
        user2 = new User("other");

        game = new Game(511, 1,  user1, user2, GameState.FINISHED, BoardType.STANDARD, Language.NL);
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
        GameController gameController = new ControllerFactory().getGameController();
        gameController.loadGame(game, Role.PLAYER);
        game.setBoardStateTo(game.getTurns().get(game.getTurns().size() - 1));
    }
}