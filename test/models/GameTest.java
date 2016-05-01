package models;

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

        game = new Game(1, user1, user2, GameState.FINISHED, BoardType.STANDARD, Language.NL);

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
}